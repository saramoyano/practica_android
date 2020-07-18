package com.dam.t07p03.vista.fragmentos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;

public class PrefFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Departamento login = null;
        if (getArguments() != null) {
            login = getArguments().getParcelable("login");
        }
        if (login != null && login.getId() == 0) { // admin
            Preference pref;
            pref = findPreference(getString(R.string.SQLite_name_key));
            if (pref != null) pref.setVisible(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Esto no funciona bien, ya que cuando un usuario no admin cambia un valor,
        // vuelve a poner el valor por defecto, no el que había!!
        // Lo he mejorado haciendo visible e invisible la preferencia en onCreatePreferences,
        // el código siguiente lo dejo para mostrar el Toast de cambio de datos!!
        Departamento login = null;
        if (getArguments() != null) {
            login = getArguments().getParcelable("login");
        }
        if (key.equals(getResources().getString(R.string.SQLite_name_key))) {
            if (login != null && login.getId() == 0) { // admin
                Toast.makeText(requireActivity(), R.string.msg_CambioBD, Toast.LENGTH_LONG).show();
            } else {
                sharedPreferences.edit().putString(key, getResources().getString(R.string.SQLite_name_default)).apply();
                Toast.makeText(requireActivity(), R.string.msg_LoginPermisos, Toast.LENGTH_LONG).show();
            }
        }
    }

}
