package fx.utill;

import javafx.scene.paint.Color;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Diverses fonctions static.
 * <ul><li>{@link Dlib#fxColorStrToRGBa(Color)}</li>
 * <li>{@link Dlib#toUpperFirst(String)}</li>
 * <li></li>
 * </ul>
 * @see Color
 * @author dinkyd.
 */
public class Dlib {

    /**
     * Converti le color.toString en String css couleur RGBa.
     * Permet l'utilisation des constantes definie dans {@link Color} dans les (string) styles css en java.
     * @see Color
     * @param color
     * @return
     */
    public static String fxColorStrToRGBa(Color color) {
        return String.format(Locale.US, "rgba(%.2f, %.2f, %.2f, %.2f)",
                color.getRed() * 255, color.getGreen() * 255, color.getBlue() * 255, color.getOpacity() * 255
        );
    }

    /**
     * Retourne le String passé en parametre avec la premiere lettre en majuscule.
     * @param str
     * @return Str
     */
    public static String toUpperFirst(String str){
        if(str == null)
            return  null;

        char[] tC = str.toCharArray();
        tC[0] = Character.toUpperCase(tC[0]);
        return new String(tC);
    }

    /**
     * Converti un String en int en supriman les characteres de
     * separation courant de numero de telephone.
     * /!\ à la plage des ints, pas genant pour la plage française. (21.47.483 647)
     * @param strTel
     * @return intTel ou -1 si numero invalid.
     * @support <ul><li>Numero fr :</li></ul>
     * @support Separation '-', '/', '.' ou ' '.
     */
    public static int strTel2Int(String strTel){

        if( !isOkPhoneInput(strTel))
            return -1;

        Pattern regSpec = Pattern.compile("[-/. ]");
        String str = regSpec.matcher(strTel).replaceAll("");

        System.out.println(regSpec.matcher(strTel).replaceAll(""));

        return  Integer.parseInt(str);
    }
    /**
     * Verifie si le String phoneInput est un numero de telephone valide
     * @param phoneInput
     * @return bool
     */
    public static boolean isOkPhoneInput(String phoneInput) {
        Boolean isOk = false;

        //pour tel fr
        Pattern regFr = Pattern.compile("^0[1-9]([-/. ]?[0-9]{2}){4}$");
        if (regFr.matcher(phoneInput).matches())
            isOk = true;
        //TODO isOkPhoneInput() autres !=regions

        return isOk;
    }
}
