package com.example.bilal.ui.server;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bilal.R;
import com.example.bilal.data.model.Server;
import com.example.bilal.databinding.FragmentServerBinding;
import com.example.bilal.ui.VpnViewModel;
import java.util.ArrayList;
import java.util.List;

public class ServerFragment extends Fragment {

    private FragmentServerBinding binding;
    private VpnViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentServerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(requireActivity()).get(VpnViewModel.class);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("United States", "37.120.202.186", "https://flagcdn.com/w80/us.png", "us"));
        servers.add(new Server("Canada", "192.168.1.55", "https://flagcdn.com/w80/ca.png", "ca"));
        servers.add(new Server("United Kingdom", "45.142.120.10", "https://flagcdn.com/w80/gb.png", "gb"));
        servers.add(new Server("Germany", "88.99.10.150", "https://flagcdn.com/w80/de.png", "de"));
        servers.add(new Server("France", "51.15.20.100", "https://flagcdn.com/w80/fr.png", "fr"));
        servers.add(new Server("Japan", "103.20.50.12", "https://flagcdn.com/w80/jp.png", "jp"));
        servers.add(new Server("Australia", "1.1.1.1", "https://flagcdn.com/w80/au.png", "au"));
        servers.add(new Server("Netherlands", "94.120.10.20", "https://flagcdn.com/w80/nl.png", "nl"));
        servers.add(new Server("Singapore", "111.90.150.1", "https://flagcdn.com/w80/sg.png", "sg"));
        servers.add(new Server("India", "103.21.150.22", "https://flagcdn.com/w80/in.png", "in"));
        servers.add(new Server("Brazil", "177.20.100.5", "https://flagcdn.com/w80/br.png", "br"));
        servers.add(new Server("South Korea", "210.92.10.55", "https://flagcdn.com/w80/kr.png", "kr"));
        servers.add(new Server("Turkey", "185.10.20.30", "https://flagcdn.com/w80/tr.png", "tr"));
        servers.add(new Server("Italy", "93.10.50.80", "https://flagcdn.com/w80/it.png", "it"));
        servers.add(new Server("Spain", "80.20.30.40", "https://flagcdn.com/w80/es.png", "es"));

        ServerAdapter adapter = new ServerAdapter(servers, server -> {
            viewModel.selectServer(server);
            Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
        });

        binding.rvServers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvServers.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}