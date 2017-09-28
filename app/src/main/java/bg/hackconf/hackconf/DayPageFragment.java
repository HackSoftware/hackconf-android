package bg.hackconf.hackconf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import bg.hackconf.hackconf.models.Talk;

public class DayPageFragment extends Fragment {

    private List<Talk> talks;

    public DayPageFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.day_page, container, false);

        talks = Parcels.unwrap(getArguments().getParcelable("talks"));


        RecyclerView scheduleRecyclerView = root.findViewById(R.id.scheduleRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        scheduleRecyclerView.setLayoutManager(layoutManager);
        TalkAdapter adapter = new TalkAdapter(talks);
        scheduleRecyclerView.setAdapter(adapter);

        return root;
    }
}
