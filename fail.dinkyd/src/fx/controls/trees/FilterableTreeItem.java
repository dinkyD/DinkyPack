package fx.controls.trees;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;

import java.lang.reflect.Field;

/**
 * Created by dinkyd.
 */
public class FilterableTreeItem<T> extends TreeItem{

    final private ObservableList<TreeItem<T>> sourceList;
    private FilteredList<TreeItem<T>> filteredList;

    private ObjectProperty<TreeItemPredicate<T>> predicate = new SimpleObjectProperty<>();

    public FilterableTreeItem(T value) {
        super(value);
        this.sourceList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(this.sourceList);
        this.filteredList.predicateProperty().bind(
                Bindings.createObjectBinding(
                        () -> child -> {
                            // Set the predicate of child items to force filtering
                            if (child instanceof FilterableTreeItem) {
                                FilterableTreeItem<T> filterableChild = (FilterableTreeItem<T>) child;
                                filterableChild.setPredicate(this.predicate.get());
                            }
                            // sans predicate pas à chercher
                            if (this.predicate.get() == null)
                                return true;
                            //conserve l'objet si children
                            if (child.getChildren().size() > 0)
                                return true;
                            // si y' a un predicate et pas de sous branche( children), voir avec le predicate
                            return this.predicate.get().test(this, child.getValue());
                        }, this.predicate
                )
        );
        setHiddenFieldChildren(this.filteredList);
    }

    protected void setHiddenFieldChildren(ObservableList<TreeItem<T>> list) {
        try {
            Field childrenField = TreeItem.class.getDeclaredField("children");
            childrenField.setAccessible(true);
            childrenField.set(this, list);

            Field declaredField = TreeItem.class.getDeclaredField("childrenListener");
            declaredField.setAccessible(true);
            list.addListener((ListChangeListener<? super TreeItem<T>>) declaredField.get(this));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Could not set TreeItem.children", e);
        }
    }

    /**
     * Getter pour le predicate.
     * @return predicateProperty
     */
    public final ObjectProperty<TreeItemPredicate<T>> predicateProperty(){
        return this.predicate;
    }
    /**
     * Set predicate
     * @param predicate
     */
    public final void setPredicate(TreeItemPredicate<T> predicate){
        this.predicate.set(predicate);
    }

    public ObservableList<TreeItem<T>> getInternalChildren() {
        return this.sourceList;
    }
}
