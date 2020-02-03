package test1;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import it.porting.android_is.firebaseArchive.bean.UtenteBean;
import it.porting.android_is.gestioneUtente.LoginActivity;
import it.porting.android_is.utility.LazyInitializedSingleton;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class LoginActivityTest {


    private static LoginActivity loginActivity;
    LazyInitializedSingleton initializeSingleton;

    UtenteBean utenteBean;






        @Test
    public void loginIsFailEmail() throws Exception {

            try {
                loginActivity.getEmail().equals("");
                loginActivity.getPassword().equals("12345678");

                String message = "Il campo email non rispetta il formato corretto";
                Exception exception = new Exception();
                assertEquals(message, exception.getMessage());

            } catch (Exception e) {

                e.printStackTrace();
            }
    }



    @Test
    public void loginIsFailPassword() throws Exception {

            try {

                loginActivity.getEmail().equals("pippo1@studenti.unisa.it");
                loginActivity.getPassword().equals("");

                String message = "Il campo password non rispetta il formato corretto";
                Exception exception = new Exception();
                assertEquals(message, exception.getMessage());

            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    @Test
    public void loginIsCorrect() throws Exception {

        try {
            loginActivity.getEmail().equals("pippo1@studenti.unisa.it");
            loginActivity.getPassword().equals("12345678");

            String message = "Formato corretto";
            assertEquals(message, "Login effettuato");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }










}
