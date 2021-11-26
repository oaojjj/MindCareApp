package com.teamopendata.mindcareapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teamopendata.mindcareapp.R;
import com.teamopendata.mindcareapp.common.model.entity.Task;
import com.teamopendata.mindcareapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // dummy data
        ArrayList<Task> dummyTasks = new ArrayList<>();
        dummyTasks.add(new Task("오늘 병원 가기"));
        dummyTasks.add(new Task("1시간 명상 하기"));
        dummyTasks.add(new Task("즐거운 생각 하기"));
        dummyTasks.add(new Task("길게 적으면 글자가 짤려요@@@@@@@@@@@@"));

        ArrayList<String> dummyKeywords = new ArrayList<>();
        dummyKeywords.add("불면");
        dummyKeywords.add("스트레스");
        dummyKeywords.add("외래,입원(입소)");
        dummyKeywords.add("공포증");

        // listView
        binding.lvKeywords
                .setAdapter(new ArrayAdapter<>(requireContext(), R.layout.item_home_keywords, dummyKeywords));

        // recyclerView
        TodayTaskAdapter adapter = new TodayTaskAdapter(dummyTasks);
        binding.rvTodayTask.setAdapter(adapter);
        // binding.rvTodayTask.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}