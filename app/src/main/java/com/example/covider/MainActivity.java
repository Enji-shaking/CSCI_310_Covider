package com.example.covider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.covider.database.ManagerFactory;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private View mapButton = null;
    private View reportButton = null;
    private View profileButton = null;
    private View mapView = null;
    private View reportView = null;
    private View profileView = null;
    private LinearLayout buildingsMap = null;
    private LinearLayout buildingsList = null;
    private final HashMap<String, Boolean> answers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        System.out.println(System.currentTimeMillis());
        getApplicationContext().deleteDatabase("covider"); // clear database for debug use
        ManagerFactory.initialize(getApplicationContext());
        initializeNavBottom();
        initializeAnswers();
        initializeHealthAnswers();
        initializeUserProfile();
        ScrollView vertical = findViewById(R.id.usc_map_vertical);
        HorizontalScrollView horizontal = findViewById(R.id.usc_map_horizontal);
        horizontal.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(() ->
                horizontal.scrollTo(horizontal.getChildAt(0).getWidth()/2,0));
        vertical.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(() ->
                vertical.scrollTo(0, vertical.getChildAt(0).getHeight()/2));
        initializeMapView();
    }

    private void initializeNavBottom() {
        if (mapButton == null) {
            mapButton = findViewById(R.id.map);
        }
        if (reportButton == null) {
            reportButton = findViewById(R.id.report);
        }
        if (profileButton == null) {
            profileButton = findViewById(R.id.profile);
        }
        if (mapView == null) {
            mapView = findViewById(R.id.map_view);
        }
        if (reportView == null) {
            reportView = findViewById(R.id.report_view);
        }
        if (profileView == null) {
            profileView = findViewById(R.id.profile_view);
        }
        ImageButton.OnClickListener bottomNavListener = (View view) -> {
            view.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal_selected)));
            String content = view.getContentDescription().toString();
            switch (content) {
                case "Map":
                    mapView.setVisibility(View.VISIBLE);
                    reportView.setVisibility(View.INVISIBLE);
                    profileView.setVisibility(View.INVISIBLE);
                    reportButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    profileButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    break;
                case "Report":
                    reportView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                    profileView.setVisibility(View.INVISIBLE);
                    mapButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    profileButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    break;
                case "Profile":
                    profileView.setVisibility(View.VISIBLE);
                    reportView.setVisibility(View.INVISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                    reportButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    mapButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    break;
            }
        };
        mapButton.setOnClickListener(bottomNavListener);
        reportButton.setOnClickListener(bottomNavListener);
        profileButton.setOnClickListener(bottomNavListener);
    }

    private void initializeAnswers() {
        answers.put("infection", null);
        answers.put("chills_fever", null);
        answers.put("taste_smell", null);
        answers.put("muscle", null);
        answers.put("cough", null);
        answers.put("breathing", null);
        answers.put("conjunctivitis", null);
        answers.put("gi_symptoms", null);
    }

    private void initializeHealthAnswers() {
        Button.OnClickListener listener = (View view) -> {
            Button clicked = (Button)view;
            clicked.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
            clicked.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gold)));
            String answer = ((Button)view).getText().toString();
            String description = view.getContentDescription().toString();
            if (answer.equals("Yes")) {
                answers.put(description, true);
                int oppositeAnswerId = getResources().getIdentifier(description+"_no", "id", getPackageName());
                Button opposite = findViewById(oppositeAnswerId);
                opposite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.switch_off_track)));
                opposite.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            } else if (answer.equals("No")) {
                answers.put(description, false);
                int oppositeAnswerId = getResources().getIdentifier(description+"_yes", "id", getPackageName());
                Button opposite = findViewById(oppositeAnswerId);
                opposite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.switch_off_track)));
                opposite.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            }
        };
        findViewById(R.id.infection_yes).setOnClickListener(listener);
        findViewById(R.id.infection_no).setOnClickListener(listener);
        findViewById(R.id.chills_fever_yes).setOnClickListener(listener);
        findViewById(R.id.chills_fever_no).setOnClickListener(listener);
        findViewById(R.id.taste_smell_yes).setOnClickListener(listener);
        findViewById(R.id.taste_smell_no).setOnClickListener(listener);
        findViewById(R.id.muscle_yes).setOnClickListener(listener);
        findViewById(R.id.muscle_no).setOnClickListener(listener);
        findViewById(R.id.cough_yes).setOnClickListener(listener);
        findViewById(R.id.cough_no).setOnClickListener(listener);
        findViewById(R.id.breathing_yes).setOnClickListener(listener);
        findViewById(R.id.breathing_no).setOnClickListener(listener);
        findViewById(R.id.conjunctivitis_yes).setOnClickListener(listener);
        findViewById(R.id.conjunctivitis_no).setOnClickListener(listener);
        findViewById(R.id.gi_symptoms_yes).setOnClickListener(listener);
        findViewById(R.id.gi_symptoms_no).setOnClickListener(listener);
    }

    private void initializeUserProfile() {
        ((TextView)findViewById(R.id.username)).setText(Html.fromHtml("Hi, <b>Trojan</b>!"));

    }

    private void initializeMapView() {
        initializeMapBuildings();
        if (buildingsMap == null) {
            buildingsMap = findViewById(R.id.usc_map_view);
        }
        if (buildingsList == null) {
            buildingsList = findViewById(R.id.usc_list_view);
        }
        Switch viewToggleSwitch = findViewById(R.id.toggle_view);
        viewToggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buildingsMap.setVisibility(View.VISIBLE);
                buildingsList.setVisibility(View.INVISIBLE);
            } else {
                buildingsMap.setVisibility(View.INVISIBLE);
                buildingsList.setVisibility(View.VISIBLE);
            }
        });
        initializeListBuildings();
    }

    private void initializeMapBuildings() {
        View.OnClickListener buildingListener = (View view) -> {
            String code = view.getContentDescription().toString();
            int stringId = getResources().getIdentifier(
                    code + "_full", "string", getPackageName());
            String fullName = getResources().getString(stringId);
            System.out.println(fullName);
        };
        findViewById(R.id.usc_map_esh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mcc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_flt).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_koh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_wto).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_lrc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_uac).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jef).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_den).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_kdc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_urc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_clh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_uuc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ahn).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jep).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ksh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_swc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mrf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ois).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_dcc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mtx).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_drc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sci).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_scb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_scc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sca).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_scx).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sce).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_aes).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ctv).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jws).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_tmc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mus).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_rhm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_bmh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_thh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_cas).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_wph).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sos).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_lvl).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jmc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_gap).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_her).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_lts).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ctf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mar).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_drb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_kap).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_rri).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mcb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sgm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_gfs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_hnb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_dps).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_scd).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ttl).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ger).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_pks).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_prb1).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_prb2).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_eeb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_rth).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ohe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_pce).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_hce).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_bhe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ssc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sal).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sel).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_phe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_vhe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_iyh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_wah).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_lhi).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_rrb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ann).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ped).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_asc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_bit).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_nct).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_adm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_bks).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sto).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_hsh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_tcc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_stu).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_sks).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_cem).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ljs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_zhs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_slh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_acb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_har).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mhp).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_bri).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_acc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_hoh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ahf1).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_hmm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ahf2).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jhh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_tgf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_law).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_vpd).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_rgl).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_dml).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_cpa).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_bsr).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_nrc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ptd).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_dxm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_alm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_dmt).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_tro).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_fig).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jkp).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_jff).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ush).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ugb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_gec).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ugw).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_shr).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_elb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_cdf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_cal).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_ift).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_rzc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_scs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_trh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_huc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_hjc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_osp).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_crc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_nbc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mrc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_cic).setOnClickListener(buildingListener);
        findViewById(R.id.usc_map_mhc).setOnClickListener(buildingListener);
    }

    private void initializeListBuildings() {
        LinearLayout.OnClickListener buildingListener = (View view) -> {
            String code = view.getContentDescription().toString();
            int stringId = getResources().getIdentifier(
                    code + "_display", "string", getPackageName());
            String displayName = getResources().getString(stringId);
            System.out.println(displayName);
        };
        findViewById(R.id.usc_list_esh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mcc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_flt).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_koh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_wto).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_lrc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_uac).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jef).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_den).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_kdc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_urc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_clh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_uuc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ahn).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jep).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ksh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_swc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mrf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ois).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_dcc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mtx).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_drc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sci).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_scb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_scc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sca).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_scx).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sce).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_aes).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ctv).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jws).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_tmc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mus).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_rhm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_bmh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_thh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_cas).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_wph).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sos).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_lvl).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jmc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_gap).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_her).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_lts).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ctf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mar).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_drb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_kap).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_rri).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mcb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sgm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_gfs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_hnb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_dps).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_scd).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ttl).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ger).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_pks).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_prb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_eeb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_rth).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ohe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_pce).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_hce).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_bhe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ssc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sal).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sel).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_phe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_vhe).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_iyh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_wah).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_lhi).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_rrb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ann).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ped).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_asc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_bit).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_nct).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_adm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_bks).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sto).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_hsh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_tcc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_stu).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_sks).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_cem).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ljs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_zhs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_slh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_acb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_har).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mhp).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_bri).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_acc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_hoh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ahf1).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_hmm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ahf2).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jhh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_tgf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_law).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_vpd).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_rgl).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_dml).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_cpa).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_bsr).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_nrc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ptd).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_dxm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_alm).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_dmt).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_tro).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_fig).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jkp).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_jff).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ush).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ugb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_gec).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ugw).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_shr).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_elb).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_cdf).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_cal).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_ift).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_rzc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_scs).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_trh).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_huc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_hjc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_osp).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_crc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_nbc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mrc).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_cic).setOnClickListener(buildingListener);
        findViewById(R.id.usc_list_mhc).setOnClickListener(buildingListener);
    }
}