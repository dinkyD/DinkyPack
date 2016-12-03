package fx.utill;

import javafx.scene.paint.Color;

import java.util.Locale;

/**
 * Created by dinkyd on 02/12/16.
 * Diverses fonctions static.
 * <ul><li>{@link Dlib#fxColorStrToRGBa(Color)}</li>
 * <li></li>
 * </ul>
 * @see Color
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
}
