package netdb.courses.softwarestudio.freeblock;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set main grid to be gone.
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.GONE);

        // set title
        getActivity().setTitle("About us");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public void onPause() {
        // set main grid to be visible.
        View main_grid = getActivity().findViewById(R.id.grid_weekday);
        main_grid.setVisibility(View.VISIBLE);

        super.onPause();
    }
}
