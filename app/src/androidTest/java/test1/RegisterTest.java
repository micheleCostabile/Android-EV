package test1;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import it.porting.android_is.gestioneStudente.Register;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RegisterTest {


    Register register;


        @Test
    public void registerIsFailNome() throws Exception {

        try {

            register.getEtNome().equals("");
            register.getEtCognome().equals("Rossi");
            register.getEtEmail().equals("mrossi1@studenti.unisa.it");
            register.getEtPassword().equals("12345678");
            register.getEtVPassword().equals("12345678");
            register.getSex().equals("M");


            String message = "Formato non corretto";
            Exception exception = new Exception();
            assertEquals(message, exception.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
          }

    }

    @Test
    public void registerIsFailCognome() throws Exception {

        try {

            register.getEtNome().equals("Mario");
            register.getEtCognome().equals("");
            register.getEtEmail().equals("mrossi1@studenti.unisa.it");
            register.getEtPassword().equals("12345678");
            register.getEtVPassword().equals("12345678");
            register.getSex().equals("M");


            String message = "Formato non corretto";
            Exception exception = new Exception();
            assertEquals(message, exception.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    @Test
    public void registerIsFailEmail() throws Exception {

        try {

            register.getEtNome().equals("Mario");
            register.getEtCognome().equals("Rossi");
            register.getEtEmail().equals("mrossi1@live.it");
            register.getEtPassword().equals("12345678");
            register.getEtVPassword().equals("12345678");
            register.getSex().equals("M");


            String message = "Formato non corretto";
            Exception exception = new Exception();
            assertEquals(message, exception.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

        }
    }



    @Test
    public void registerIsFailurPsw() throws Exception {

        try {

            register.getEtNome().equals("Mario");
            register.getEtCognome().equals("Rossi");
            register.getEtEmail().equals("mrossi1@studenti.unisa.it");
            register.getEtPassword().equals("178");
            register.getEtVPassword().equals("12345678");
            register.getSex().equals("M");


            String message = "Formato non corretto";
            Exception exception = new Exception();
            assertEquals(message, exception.getMessage());


        } catch (Exception e) {

            e.printStackTrace();
        }
    }



    @Test
    public void registerIsFailureVPassword() throws Exception {


        try {

            register.getEtNome().equals("Mario");
            register.getEtCognome().equals("Rossi");
            register.getEtEmail().equals("mrossi1@studenti.unisa.it");
            register.getEtPassword().equals("12345678");
            register.getEtVPassword().equals("123");
            register.getSex().equals("M");


            String message = "Formato non corretto";
            Exception exception = new Exception();
            assertEquals(message, exception.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

          }
    }


    @Test
    public void registerIsFailureSex() throws Exception {


        try {

            register.getEtNome().equals("Mario");
            register.getEtCognome().equals("Rossi");
            register.getEtEmail().equals("mrossi1@studenti.unisa.it");
            register.getEtPassword().equals("12345678");
            register.getEtVPassword().equals("12345678");
            register.getSex().equals("");


            String message = "Formato non corretto";
            Exception exception = new Exception();
            assertEquals(message, exception.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

          }
    }


    @Test
    public void registerIsCorrect() throws Exception {

        try {
            register.getEtNome().equals("Mario");
            register.getEtCognome().equals("Rossi");
            register.getEtEmail().equals("mrossi1@studenti.unisa.it");
            register.getEtPassword().equals("12345678");
            register.getEtVPassword().equals("12345678");
            register.getSex().equals("M");

            String message = "Formato corretto";
            assertEquals(message, "Registrazione effettuata");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
