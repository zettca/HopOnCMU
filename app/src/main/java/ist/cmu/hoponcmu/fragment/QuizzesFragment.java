package ist.cmu.hoponcmu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ist.cmu.hoponcmu.CMUtils;
import ist.cmu.hoponcmu.R;
import ist.cmu.hoponcmu.activity.QuizAnswerActivity;
import okhttp3.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuizzesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuizzesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizzesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    static final int PICK_ANSWER_REQUEST_CODE = 1;

    public QuizzesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuizzesFragment.
     */
    public static QuizzesFragment newInstance() {
        return new QuizzesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(
                CMUtils.DATA_NAME, Context.MODE_PRIVATE);
        Set<String> locationIDs = prefs.getStringSet("locationIDs", new HashSet<String>());
        String authToken = prefs.getString("token", null);

        Log.w("TEST", String.format("Found %d locations", locationIDs.size()));

        Button refreshButton = view.findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().
                        getSystemService(Context.WIFI_SERVICE);
                if (wifiManager == null) return;

                SharedPreferences prefs = getActivity().getSharedPreferences(
                        CMUtils.DATA_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Set<String> locs = prefs.getStringSet("locationIDs", new HashSet<String>());
                String authToken = prefs.getString("token", null);

                boolean wifiOn = wifiManager.isWifiEnabled();
                String ssid = wifiManager.getConnectionInfo().getSSID();
                Log.w("DATA", ssid);
                locs.add((wifiOn) ? ssid : "M14");
                editor.putStringSet("locationIDs", locs);
                editor.apply();

                showLocations(v.getRootView(), locs, authToken);
            }
        });

        showLocations(view, locationIDs, authToken);

        return view;
    }

    private void showLocations(@NonNull View view, Set<String> locationIDs, String authToken) {
        LinearLayout layoutQuizzes = view.findViewById(R.id.layout_quizzes);
        if (layoutQuizzes.getChildCount() > 0) layoutQuizzes.removeAllViews();
        for (String locID : locationIDs) {
            new GetQuizTask().execute(locID, authToken);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int[] answers = data.getIntArrayExtra("ANSWERS");

        if (requestCode == PICK_ANSWER_REQUEST_CODE) {
            Toast.makeText(getActivity(), "GOT: " + Arrays.toString(answers), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Unexpected stuffs...", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class GetQuizTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            final String locationID = params[0];
            final String authToken = params[1];
            final String getParams = "?location=" + locationID;
            Response response = CMUtils.getData("quizzes", getParams, authToken);

            try {
                final String data = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout layoutQuizzes = getView().findViewById(R.id.layout_quizzes);

                        Button button = new Button(getActivity());
                        button.setText("Answer quiz " + locationID);
                        button.setLayoutParams(
                                new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), QuizAnswerActivity.class);
                                intent.putExtra("DATA", data);
                                startActivityForResult(intent, PICK_ANSWER_REQUEST_CODE);
                            }
                        });
                        layoutQuizzes.addView(button);

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
