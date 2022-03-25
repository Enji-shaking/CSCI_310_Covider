package com.example.covider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covider.database.ManagerFactory;
import com.example.covider.database.building.BuildingManager;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.notification.NotificationManager;
import com.example.covider.database.report.ReportManager;
import com.example.covider.database.risk.RiskManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.building.Building;
import com.example.covider.model.course.Course;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.notification.Notification;
import com.example.covider.model.report.BuildingRiskReport;
import com.example.covider.model.report.UserDailyReport;
import com.example.covider.model.user.Student;
import com.example.covider.model.user.User;
import com.google.android.material.navigation.NavigationBarItemView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private View mapButton = null;
    private View reportButton = null;
    private View profileButton = null;
    private View notificationButton = null;
    private View mapView = null;
    private View reportView = null;
    private View profileView = null;
    private View notificationView = null;
    private LinearLayout buildingsMap = null;
    private LinearLayout buildingsList = null;
    private String userName = null;
    private long userId = 0;
    private int isStu = 0;
    private final HashMap<String, Boolean> answers = new HashMap<>();

    private void createDummy(){
        RiskManager riskManager;
        EnrollmentManager enrollmentManager;
        CheckinManager checkinManager;
        ReportManager reportManager;
        CourseManager courseManager;
        UserManager userManager;
        NotificationManager notificationManager;
        BuildingManager buildingManager;

        riskManager = ManagerFactory.getRiskManagerInstance();
        reportManager = ManagerFactory.getReportManagerInstance();
        checkinManager = ManagerFactory.getCheckinManagerInstance();
        enrollmentManager = ManagerFactory.getEnrollmentManagerInstance();
        courseManager = ManagerFactory.getCourseManagerInstance();
        userManager = ManagerFactory.getUserManagerInstance();
        notificationManager = ManagerFactory.getNotificationManagerInstance();
        buildingManager = ManagerFactory.getBuildingManagerInstance();

        reportManager.addOrUpdateReport(new UserDailyReport(10009, 9, 1, "Stay Positive", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(10010, 10, 0, "Fever", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(10011, 11, 0, "", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(10012, 12, 1, "", System.currentTimeMillis()));

        checkinManager.addCheckin(9, 99);
        checkinManager.addCheckin(10, 99);
        checkinManager.addCheckin(11, 99);

        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1009, 9, 101, 1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1010, 10,101,1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1011, 11,101,1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 1012, 12, 101,0));

        enrollmentManager.addEnrollment(1, 1, 1);
        enrollmentManager.addEnrollment(1, 2, 1);
        enrollmentManager.addEnrollment(2, 1, 1);
        enrollmentManager.addEnrollment(2, 2, 1);
        enrollmentManager.addEnrollment(3, 1, 0);
        enrollmentManager.addEnrollment(3, 2, 0);

        courseManager.addOrUpdateCourse(new Course(101,"Course For Testing", 99, 0));

        userManager.addOrUpdateUser(new User(9,"RiskTester1", "12345678", 1));
        userManager.addOrUpdateUser(new User(10,"RiskTester2", "12345678", 1));
        userManager.addOrUpdateUser(new User(11,"RiskTester3", "12345678", 1));
        userManager.addOrUpdateUser(new User(12,"RiskTester4", "12345678", 0));

        notificationManager.addOrUpdateNotification(new Notification(1009, 11, 10, 0,"Testing notification"));

        buildingManager.addBuilding("SA"); // will be overwritten by the next line
        buildingManager.addOrUpdateBuilding(new Building(91,"SAL"));
        buildingManager.addOrUpdateBuilding(new Building(92,"KAP"));
        buildingManager.addOrUpdateBuilding(new Building(94,"LVL"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        System.out.println(System.currentTimeMillis());
        getApplicationContext().deleteDatabase("covider"); // clear database for debug use
        ManagerFactory.initialize(getApplicationContext());
        createDummy();
        initializeLogInPage();
        initializeNavBottom();
        initializeReportPage();
        initializeUserProfile();
        initializeMapView();
    }

    private void initializeLogInPage() {
        View.OnFocusChangeListener ofcListener = (View view, boolean hasFocus) -> {
            EditText usernameInput = findViewById(R.id.log_in_username);
            EditText passwordInput = findViewById(R.id.log_in_password);
            if(!hasFocus && !usernameInput.hasFocus() && !passwordInput.hasFocus()) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        };
        findViewById(R.id.log_in_username).setOnFocusChangeListener(ofcListener);
        findViewById(R.id.log_in_password).setOnFocusChangeListener(ofcListener);
        Button.OnClickListener logInListener = (View view) -> {
            View temp = this.getCurrentFocus();
            if (temp != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(temp.getWindowToken(), 0);
            }
            EditText usernameInput = findViewById(R.id.log_in_username);
            EditText passwordInput = findViewById(R.id.log_in_password);
            String userNameTmp = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            UserManager um = ManagerFactory.getUserManagerInstance();
            User user = um.getUserByName(userNameTmp);
            if (user == null){
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Error")
                        .setMessage("User does not exist")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Log In Failed",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
            else if (user.getPassword().equals(password)) {
                userName = user.getName();
                userId = user.getId();
                isStu = user.getIsStudent();
                ((TextView)findViewById(R.id.username)).setText(
                        Html.fromHtml("Hi, <b>" + userName + "</b>!"));
                mapView.setVisibility(View.VISIBLE);
                reportView.setVisibility(View.INVISIBLE);
                profileView.setVisibility(View.INVISIBLE);
                notificationView.setVisibility(View.INVISIBLE);
                mapButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal_selected)));
                reportButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                profileButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                notificationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                findViewById(R.id.log_in_view).setVisibility(View.INVISIBLE);

            }
            else{
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Error")
                        .setMessage("Wrong Password")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Log In Failed",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }


        };
        findViewById(R.id.log_in_submit).setOnClickListener(logInListener);
    }

    private void changeCourseStatus(long courseId){
        CourseManager cm = ManagerFactory.getCourseManagerInstance();
        cm.toggleClassInPersonOnlineStatus(userId, courseId);
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
        if (notificationButton == null) {
            notificationButton = findViewById(R.id.notification);
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
        if (notificationView == null) {
            notificationView = findViewById(R.id.notification_view);
        }
        ImageButton.OnClickListener bottomNavListener = (View view) -> {
            view.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal_selected)));
            String content = view.getContentDescription().toString();
            switch (content) {
                case "Map":
                    mapView.setVisibility(View.VISIBLE);
                    reportView.setVisibility(View.INVISIBLE);
                    profileView.setVisibility(View.INVISIBLE);
                    notificationView.setVisibility(View.INVISIBLE);
                    reportButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    profileButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    notificationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    break;
                case "Report":
                    reportView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                    profileView.setVisibility(View.INVISIBLE);
                    notificationView.setVisibility(View.INVISIBLE);
                    mapButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    profileButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    notificationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    break;
                case "Profile":
                    profileView.setVisibility(View.VISIBLE);
                    reportView.setVisibility(View.INVISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                    notificationView.setVisibility(View.INVISIBLE);
                    reportButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    mapButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    notificationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    // TODO: front end show all reports
                    ReportManager rm = ManagerFactory.getReportManagerInstance();
                    ArrayList<UserDailyReport> reports = rm.getUserMostRecentReportsTopK(userId, 10);
                    System.out.println(reports);
                    // [Report{id=10013, userId=10, isPositive=1, note='infection,breathing,gi_symptoms,taste_smell,muscle,chills_fever,conjunctivitis,cough,', timestamp=1648177952924}, Report{id=10010, userId=10, isPositive=0, note='Fever', timestamp=1648177932202}]

                    // TODO: show enrollment
                    EnrollmentManager em = ManagerFactory.getEnrollmentManagerInstance();
                    ArrayList<Course> courses;
                    if(isStu == 0){
                        courses = em.getCoursesTaughtBy(userId);
                    }else {
                        courses = em.getCoursesTakenBy(userId);
                    }
                    System.out.println(courses);
                    // [Course{id=101, name='Course For Testing', building=99}]

                    // TODO: add onclick function for change status course

                    break;
                case "Notification":
                    NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
                    ArrayList<Notification> notifications = nm.getNotificationFor(userId);
                    System.out.println(notifications);
                    // [Notification{id=1009, from=11, to=10, read=0, message='Testing notification'}]
                    
                    notificationView.setVisibility(View.VISIBLE);
                    profileView.setVisibility(View.INVISIBLE);
                    reportView.setVisibility(View.INVISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                    reportButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    mapButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    profileButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                    break;
            }
        };

        mapButton.setOnClickListener(bottomNavListener);
        reportButton.setOnClickListener(bottomNavListener);
        profileButton.setOnClickListener(bottomNavListener);
        notificationButton.setOnClickListener(bottomNavListener);
    }


    private void initializeReportPage() {
        initializeAnswers();
        initializeHealthAnswers();
        Button.OnClickListener submitHealthFormHandler = (View view) -> {
            System.out.println("Submit Health Form");
            String symptom = "";
            boolean isPositive = false;
            for (HashMap.Entry<String, Boolean> set :
                    answers.entrySet()) {
                if(set.getValue() == null){
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Error")
                            .setMessage("Fill out all forms please")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(),"Fill out all forms please",Toast.LENGTH_LONG).show();
                                }
                            })
                            .show();
                    return;
                }
                if(set.getValue()){
                    symptom+=set.getKey() + ",";
                }
                if(set.getKey().equals("infection")){
                    isPositive = true;
                }
            }
            ReportManager rm = ManagerFactory.getReportManagerInstance();
            rm.addReport(userId, isPositive ? 1 : 0, symptom);

            if (isPositive){
                CheckinManager cm = ManagerFactory.getCheckinManagerInstance();
                NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
                ArrayList<Long> closeContacts = cm.getCloseContact(userId);

                for (Long closeContactUserId : closeContacts){
                    nm.addNotification(userId, closeContactUserId, "You got close contact with a positive patient, BEWARE!");
                }
            }else if(!symptom.equals("")){
                CheckinManager cm = ManagerFactory.getCheckinManagerInstance();
                NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
                ArrayList<Long> closeContacts = cm.getCloseContact(userId);
                System.out.println(closeContacts);


                for (Long closeContactUserId : closeContacts){
                    nm.addNotification(userId, closeContactUserId, "You got close contact with a student with covid related symptom, BEWARE!");
                    nm.addNotification(userId, closeContactUserId, "You got close contact with positive covid case, BEWARE!");
                }

                if (isStu == 0){
                    EnrollmentManager em = ManagerFactory.getEnrollmentManagerInstance();
                    CourseManager courseManager = ManagerFactory.getCourseManagerInstance();
                    ArrayList<Course> courses =  em.getCoursesTaughtBy(userId);
                    for (Course course : courses) {
                        courseManager.notifyOnline(userId, course.getId());
                    }
                }

            }

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_input_get)
                    .setTitle("Success!")
                    .setMessage("Your form has been recorded")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            for (HashMap.Entry<String, Boolean> set :
                    answers.entrySet()) {
                answers.put(set.getKey(), null);
                int buttonId = getResources().getIdentifier(set.getKey()+"_no", "id", getPackageName());
                Button button = findViewById(buttonId);
                button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.switch_off_track)));
                button.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                buttonId = getResources().getIdentifier(set.getKey()+"_yes", "id", getPackageName());
                button = findViewById(buttonId);
                button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.switch_off_track)));
                button.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            }



            System.out.println(answers);
        };
        findViewById(R.id.submit_health_form).setOnClickListener(submitHealthFormHandler);
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
        Button.OnClickListener logOutListener = (View view) -> {
            EditText usernameInput = findViewById(R.id.log_in_username);
            usernameInput.setText(null);
            EditText passwordInput = findViewById(R.id.log_in_password);
            passwordInput.setText(null);
            userName = null;
            userId = 0;
            findViewById(R.id.log_in_view).setVisibility(View.VISIBLE);
        };
        findViewById(R.id.log_out_button).setOnClickListener(logOutListener);
    }

    private void initializeMapView() {
        ScrollView vertical = findViewById(R.id.usc_map_vertical);
        HorizontalScrollView horizontal = findViewById(R.id.usc_map_horizontal);
        horizontal.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(() ->
                horizontal.scrollTo(horizontal.getChildAt(0).getWidth()/2,0));
        vertical.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(() ->
                vertical.scrollTo(0, vertical.getChildAt(0).getHeight()/2));
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
            int stringIdTmp = getResources().getIdentifier(
                    code + "_full", "string", getPackageName());

            int buildingIdTmp = getResources().getIdentifier(
                    code + "_id", "string", getPackageName());

            String fullName = getResources().getString(stringIdTmp);
            long buildingId = Long.parseLong(getResources().getString(buildingIdTmp));
            System.out.println(fullName + " " + buildingId);
            RiskManager rm = ManagerFactory.getRiskManagerInstance();
            BuildingRiskReport brp = rm.getReportForBuilding(buildingId);
            System.out.println(brp);
            // TODO: show building report on click
            // BuildingRiskReport{description='Last 2 Days', spanStartTime=1647999432901, spanEndTime=1648172232901, numVisitors=3, numLowRiskVisitors=1, numHighRiskVisitors=1, numPositiveVisitors=1}
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