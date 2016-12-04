package fx.controls.fields;

import fx.utill.Dlib;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * {@link TextField} simplifiant la creation de champs non nullable en permettant d' indiquer le nombre de characteres desiré,
 * une bordure coloré en fonction de la validité de l'input et le choix de la couleur à appliquer.
 * Indiquer {@code null}" si la colorisation des bordures n'est pas desiré.
 *
 * @author dinkyd
 * @Couleur : {@link javafx.scene.paint.Color}. (vert pour valid et rouge invalid par default.)
 * @min&max int. (minimum 1 charactere, sans max par default.)
 */
public class NotNullStrField extends TextField {

    private String okColor, nonOkColor;

    private BooleanProperty valid;

    /**
     * Provoque el {@link javafx.beans.value.ChangeListener}.
     * Utile si verification demand&eacute; sans changement d' etat de {@code valid}
     */
    public void apply(){
        applied.setValue(!applied.get());
    }
    private BooleanProperty applied;

    public boolean isValid() {
        if(!applied.get() && !valid.get()){//pourquoi colorer si tout est ok ?
            apply();
        }
        return valid.get();
    }
    public BooleanProperty validProperty() {
        return valid;
    }

    /**
     * Place la validation à true.
     * Ou presque..
     */
    public void forceValidate() {
        System.err.println(
                "Hey pov naz, prend un textField si tu veux forcer la validation !! :fp:"
        );
    }

    /**
     * Positionne la validation à false.
     */
    public void setInvalid(){
        valid.setValue(false);
    }

    /**
     * Contructeur par default.
     * nb de characteres attendu 1+.
     * Bordure verte si valid, Rouge si non.
     */
    public NotNullStrField() {
        this(1,0);
    }

    /**
     * Constructeur avec nombre de characteres attendu en parametres.
     * Indiquer <code>0</code> pour paremetre non desiré.
     * @param charMin
     * @param charMax
     */
    public NotNullStrField(int charMin, int charMax){
        this(charMin, charMax, Color.GREEN, Color.RED);
    }
    /**
     * Creer un TextField indiquant si les parametres de validation sont respectés.
     *
     * @param minChar Integer valant le nombre minimum de characteres attendu.
     * @param maxChar Integer indiquant le nombre de characters max.
     * @param validColor Color de bordure en cas de parametres valide.
     * @param invalidColor Color de bordure en cas d' input ne correspnodant pas.
     */
    public NotNullStrField(int minChar, int maxChar, Color validColor, Color invalidColor){
        super();
        if(maxChar>0 && minChar>maxChar)
            throw new ArithmeticException(
                    "Max attendu inferieur au minimum.. Mais bien sûr...\n\t" +
                    "Mininmum de characteres = " + minChar+"\n\tMaximum de characteres = " + maxChar);

        this.nonOkColor = Dlib.fxColorStrToRGBa(invalidColor);
        this.okColor = Dlib.fxColorStrToRGBa(validColor);

        applied = new SimpleBooleanProperty(false);
        valid = new SimpleBooleanProperty();
        this.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue.length() >= minChar){
                        if(maxChar == 0 || maxChar >= newValue.length()){
                            valid.setValue(true);
                        }else{
                            valid.set(false);
System.out.println("NotNullStrField->Change(): " +(newValue.length() - maxChar)+" characteres en trop.");
                        }
                    }else{
                        valid.set(false);
System.out.println("NotNullStrField->Change(): Manque " + (minChar - newValue.length() ) + " characters.");
                    }
          }
        );

        BooleanProperty[] boolTab = {applied,valid};
        for (BooleanProperty bool : boolTab) {
            bool.addListener((observable, oldValue, newValue) -> {
                if( valid.get() == false){
                    this.styleProperty().setValue("-fx-border-color:" + nonOkColor);//invalidColor.toString().substring(2));
                }else{
                    this.styleProperty().setValue(String.format("-fx-border-color:%s", okColor ));// validColor));
                }
            });
        }
    }
}
