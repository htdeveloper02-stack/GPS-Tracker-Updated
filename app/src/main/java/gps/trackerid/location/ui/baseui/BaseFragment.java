package gps.trackerid.location.ui.baseui;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.ads.module.util.Preference;

import gps.trackerid.location.utils.LocaleHelper;


public class BaseFragment extends Fragment {
    private Preference preference;

    @Override
    public void onAttach(Context context) {
        preference = new Preference(context);
        String lang = preference.getSavedLanguage(); // get saved language
        LocaleHelper.setLocale(context, lang); // apply language
        super.onAttach(context);
    }

    /**
     * Call this method to change language dynamically in a fragment
     * @param languageCode - e.g., "en", "es", "fr"
     */
    public void changeFragmentLanguage(String languageCode) {
        preference.saveLanguage(languageCode);
        Context context = getContext();
        if (context != null) {
            LocaleHelper.setLocale(context, languageCode);
            // Refresh the fragment to apply language changes
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commitAllowingStateLoss();
            }
        }
    }
}
