package com.dam.t07p03.vista.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.vistamodelo.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {

    private TextView tvLogin;
    private BottomNavigationView bnvMenu;

    public static final String TAG = "MainFragment";
    private MainFragInterface mListener;

    public interface MainFragInterface {
        void onClickBottomNavMainFrag(int menuItem);
    }

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(Bundle arguments) {
        MainFragment frag = new MainFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainFragInterface) {
            mListener = (MainFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MainFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // FindViewByIds
        tvLogin = v.findViewById(R.id.tvLogin);
        bnvMenu = v.findViewById(R.id.bnvMenu);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        MainViewModel mainVM = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        Departamento login = mainVM.getLogin();     // Recuperamos el login del ViewModel
        if (login != null)
            tvLogin.setText(login.getNombre());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        boolean mostrarBNav = pref.getBoolean(getResources().getString(R.string.bNav_key), false);
        if (mostrarBNav)
            bnvMenu.setVisibility(View.VISIBLE);
        else
            bnvMenu.setVisibility(View.INVISIBLE);

        // Listeners
        bnvMenu.setOnNavigationItemSelectedListener(bnvMenu_OnNavigationSelectedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bnvMenu_OnNavigationSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if (mListener != null) {
                mListener.onClickBottomNavMainFrag(menuItem.getItemId());
                return true;
            }
            return false;
        }
    };

}
