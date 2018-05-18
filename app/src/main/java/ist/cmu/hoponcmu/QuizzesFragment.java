package ist.cmu.hoponcmu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


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

        Button buttonAnswerQuiz = view.findViewById(R.id.button_answer_quiz);
        buttonAnswerQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = "How many bananas?";
                String[] options = {"2", "about 10", "0ver 9000"};

                Intent quizAnswerIntent = new Intent(getActivity(), QuizAnswerActivity.class);
                quizAnswerIntent.putExtra("QUESTION", question);
                quizAnswerIntent.putExtra("OPTIONS", options);
                startActivityForResult(quizAnswerIntent, PICK_ANSWER_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int resInt = data.getIntExtra("ANSWER", 0);

        if (requestCode == PICK_ANSWER_REQUEST_CODE) {
            Toast.makeText(getActivity(), "Got result: " + resInt, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
}
