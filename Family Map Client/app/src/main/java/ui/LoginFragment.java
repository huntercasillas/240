package ui;

import model.Model;
import net.ServerProxy;
import android.os.Bundle;
import android.view.View;
import result.LoginResult;
import android.os.AsyncTask;
import request.LoginRequest;
import android.view.Gravity;
import android.widget.Toast;
import result.RegisterResult;
import android.text.Editable;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import request.RegisterRequest;
import android.text.TextWatcher;
import android.widget.RadioGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import casillas.familymapclient.R;

public class LoginFragment extends Fragment {

    // Declare model
    private Model model;

    // Declare text edit fields
    private EditText editServerHost, editServerPort, editUserName,
            editPassword, editFirstName, editLastName, editEmail;

    // Declare buttons
    private Button loginButton, registerButton;

    // Declare strings for storing user information
    private String serverHost = "10.0.2.2";
    private String serverPort = "8080";
    private String userName = "huntercasillas";
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String gender = "";

    // Declare boolean variables
    private boolean genderSet, loginSuccess, registerSuccess;

    // Declare login and register results
    LoginResult loginResultSuccess;
    RegisterResult registerResultSuccess;

    public LoginFragment() {
        model = Model.getModel();
    }

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(new Bundle());
        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get updated model
        model = Model.getModel();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Set default booleans to false
        loginSuccess = false;
        registerSuccess = false;
        genderSet = false;

        // Assign variables to corresponding xml id's
        editServerHost = (EditText) view.findViewById(R.id.editServerHost);
        editServerHost.setText(serverHost);
        editServerPort = (EditText) view.findViewById(R.id.editServerPort);
        editServerPort.setText(serverPort);
        editUserName = (EditText) view.findViewById(R.id.editUserName);
        editUserName.setText(userName);
        editPassword = (EditText) view.findViewById((R.id.editPassword));
        editFirstName = (EditText) view.findViewById(R.id.editFirstName);
        editLastName = (EditText) view.findViewById(R.id.editLastName);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        registerButton = (Button) view.findViewById(R.id.registerButton);
        RadioGroup genderButtons = (RadioGroup) view.findViewById(R.id.genderButtons);

        // Disable login and register buttons as default
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        // Add text changed listener for login button
        editServerHost.addTextChangedListener(loginTextWatcher);
        editServerPort.addTextChangedListener(loginTextWatcher);
        editUserName.addTextChangedListener(loginTextWatcher);
        editPassword.addTextChangedListener(loginTextWatcher);

        // Add text changed and gender changed listeners for register button
        editServerHost.addTextChangedListener(registerTextWatcher);
        editServerPort.addTextChangedListener(registerTextWatcher);
        editUserName.addTextChangedListener(registerTextWatcher);
        editPassword.addTextChangedListener(registerTextWatcher);
        editFirstName.addTextChangedListener(registerTextWatcher);
        editLastName.addTextChangedListener(registerTextWatcher);
        editEmail.addTextChangedListener(registerTextWatcher);
        genderButtons.setOnCheckedChangeListener(genderChangedListener);

        // Set on click listeners for login and register buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButton();
            }
        });

        return view;
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // No need to do anything here
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Get user input and assign it to strings
            serverHost = editServerHost.getText().toString().trim();
            serverPort = editServerPort.getText().toString().trim();
            userName = editUserName.getText().toString().trim();
            password = editPassword.getText().toString();

            // Enable login button if necessary fields are filled in
            if (!serverHost.isEmpty() && !serverPort.isEmpty()
                    && !userName.isEmpty() && !password.isEmpty()) {
                loginButton.setEnabled(true);
            } else {
                loginButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // No need to do anything here
        }
    };

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // No need to do anything here
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Get user input and assign it to strings
            serverHost = editServerHost.getText().toString().trim();
            serverPort = editServerPort.getText().toString().trim();
            userName = editUserName.getText().toString().trim();
            password = editPassword.getText().toString();
            firstName = editFirstName.getText().toString().trim();
            lastName = editLastName.getText().toString().trim();
            email = editEmail.getText().toString().trim();

            // Enable register button if necessary fields are filled in
            if (!serverHost.isEmpty() && !serverPort.isEmpty()
                    && !userName.isEmpty() && !password.isEmpty() && !firstName.isEmpty()
                    && !lastName.isEmpty() && !email.isEmpty() && genderSet) {
                registerButton.setEnabled(true);
            } else {
                registerButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // No need to do anything here
        }
    };

    private RadioGroup.OnCheckedChangeListener genderChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int genderButtonID = radioGroup.getCheckedRadioButtonId();

            // Assign gender selection to its string variable
            switch (genderButtonID) {
                case R.id.maleButton:
                    gender = "m";
                    genderSet = true;
                    break;
                case R.id.femaleButton:
                    gender = "f";
                    genderSet = true;
                    break;
            }

            // Enable register button if necessary fields are filled in
            if (!serverHost.isEmpty() && !serverPort.isEmpty()
                    && !userName.isEmpty() && !password.isEmpty() && !firstName.isEmpty()
                    && !lastName.isEmpty() && !email.isEmpty() && genderSet) {
                registerButton.setEnabled(true);
            } else {
                registerButton.setEnabled(false);
            }
        }
    };

    private void loginButton() {
        LoginTask loginTask = new LoginTask();
        // Execute login task once login button is clicked
        loginTask.execute();
    }

    private void registerButton() {
        RegisterTask registerTask = new RegisterTask();
        // Execute register task once register button is clicked
        registerTask.execute();
    }

    // Get family data from the server
    public class DataSyncTask extends AsyncTask<Void, String, Void> {

        protected Void doInBackground(Void... voids) {
            // Initialize the server proxy
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.getPersons(serverHost, serverPort);
            serverProxy.getEvents(serverHost, serverPort);
            return null;
        }

        protected void onPostExecute(Void voids) {
            // Display the map fragment
            MainActivity mainActivity;
            mainActivity = (MainActivity) getContext();
            mainActivity.displayMapFragment();
        }
    }

    public class RegisterTask extends AsyncTask<Void, String, RegisterResult> {

        protected RegisterResult doInBackground(Void... voids) {
            registerSuccess = false;

            // Create a new register request
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUserName(userName);
            registerRequest.setPassword(password);
            registerRequest.setEmail(email);
            registerRequest.setFirstName(firstName);
            registerRequest.setLastName(lastName);
            registerRequest.setGender(gender);

            // Display register message update
            publishProgress("Registering user...");

            // Initialize the server proxy
            ServerProxy serverProxy = new ServerProxy();
            // Give the necessary information to server proxy in the form of register result
            RegisterResult registerResult;
            registerResult = serverProxy.registerUser(serverHost, serverPort, registerRequest);
            return registerResult;
        }

        protected void onProgressUpdate(String... updateMessage) {
            Toast.makeText(getContext(), updateMessage[0], Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(RegisterResult registerResult) {
            // If there is no valid auth token, set registerSuccess to false
            if (registerResult.getAuthToken() == null) {
                registerSuccess = false;

                // If the error message from the server is null, send this toast
                if (registerResult.getUserName() == null || registerResult.getPersonID() == null) {

                    Toast toast = Toast.makeText(getContext(),
                            "Registration failed. \nThe username may already be taken. \nPlease check to make sure the entered information is correct.",
                            Toast.LENGTH_LONG);
                    LinearLayout layout = (LinearLayout) toast.getView();
                    if (layout.getChildCount() > 0) {
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }
                    toast.show();
                }
            } else {
                registerSuccess = true;
                registerResultSuccess = registerResult;

                // Set up model with given information
                model = Model.getModel();
                model.setRegisterResult(registerResult);
                model.setAuthToken(registerResult.getAuthToken());
                model.setMainPersonID(registerResult.getPersonID());
                model.setServerHost(serverHost);
                model.setServerPort(serverPort);
            }
            if (registerSuccess) {
                // If the register request is successful, get data from the server and display it on the map
                DataSyncTask dataSyncTask = new DataSyncTask();
                dataSyncTask.execute();
            }
        }
    }

    public class LoginTask extends AsyncTask<Void, String, LoginResult> {

        protected LoginResult doInBackground(Void... params) {
            loginSuccess = false;
            // Create a new login request
            LoginRequest loginRequest = new LoginRequest(userName, password);

            // Display login message update
            publishProgress("Logging in user...");

            // Initialize the server proxy
            ServerProxy serverProxy = new ServerProxy();

            // Give the necessary information to server proxy in the form of login result
            LoginResult loginResult;
            loginResult = serverProxy.loginUser(serverHost, serverPort, loginRequest);
            return loginResult;
        }

        protected void onProgressUpdate(String... updateMessage) {
            Toast.makeText(getContext(), updateMessage[0], Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(LoginResult loginResult) {
            // If there is no valid auth token, set loginSuccess to false
            if (loginResult.getAuthToken() == null) {
                loginSuccess = false;

                // If the error message from the server is null, send this toast
                if (loginResult.getUserName() == null || loginResult.getPersonID() == null) {

                    Toast toast = Toast.makeText(getContext(),
                            "Login failed. \nPlease make sure your information is correct.",
                            Toast.LENGTH_LONG);
                    LinearLayout layout = (LinearLayout) toast.getView();
                    if (layout.getChildCount() > 0) {
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }
                    toast.show();
                }
            }
            // Otherwise, the login result was successful
            else {
                loginSuccess = true;
                loginResultSuccess = loginResult;

                // Set up model with given information
                model = Model.getModel();
                model.setLoginResult(loginResult);
                model.setAuthToken(loginResult.getAuthToken());
                model.setMainPersonID(loginResult.getPersonID());
                model.setServerHost(serverHost);
                model.setServerPort(serverPort);
            }
            if (loginSuccess) {
                // If the login request is successful, get data from the server and display it on the map
                DataSyncTask dataSyncTask = new DataSyncTask();
                dataSyncTask.execute();
            }
        }
    }
}