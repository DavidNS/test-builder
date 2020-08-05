package com.dns.resttestbuilder.gui.workspace.choiceBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.gui.WindowBuilder;
import com.dns.resttestbuilder.gui.workspace.WorkspaceController;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Component
@Scope("prototype")
@Slf4j
public class WorkspaceChoiceBox<T> {

	private static final String DELETE = "Delete";

	private static final String BACK = "Back";

	private static final String NEW = "New";

	private static final String SAVE = "Save";

	private static final String EDIT = "Edit";

	@Autowired
	WorkspaceController workspaceController;

	@Autowired
	WindowBuilder wb;

	@FXML
	Button editSaveButton;

	@FXML
	Button deleteBackButton;

	@FXML
	ChoiceBox<T> choiceBox;

	@FXML
	AnchorPane choiceBoxAnchor;

	private Consumer<T> seletectFunction;

	private Consumer<T> saveFunction;

	private Consumer<T> editFunction;

	private Consumer<T> deleteFunction;

	private BiFunction<T, T, Boolean> equalsFuntion;

	private Supplier<T> newItemSupplier;

	private Function<T, String> getStringFunction;

	private BiConsumer<T, String> setStringFuntion;

	private TextField writeTextField = new TextField();

	private T oldValue;

	@FXML
	public void initialize() {
		initEditSave();
		initEditBack();
		initChoiceBox();
	}

	private void initChoiceBox() {
		choiceBox.getSelectionModel().selectedIndexProperty().addListener(this::changeListener);
	}

	private void changeListener(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
		if (newVal.intValue() != -1 && oldVal.intValue() != -1) {
			oldValue = choiceBox.getItems().get(oldVal.intValue()); // Save the previous item for the case restore
			T t = choiceBox.getItems().get(newVal.intValue());
			if (NEW.equals(getStringFunction.apply(t))) {
				prepareNewStatus();
				writeTextField.setText(NEW);
			} else {
				seletectFunction.accept(t);
			}
		}
	}

	private void initEditBack() {
		deleteBackButton.setOnAction((e) -> {
			if (BACK.equals(deleteBackButton.getText())) {
				restoreStatus(oldValue); // The case restore
			} else if (choiceBox.getItems().size() < 3) {
				wb.createAlert();
			} else {
				deleteFunction.accept(choiceBox.getSelectionModel().getSelectedItem());
			}
		});
	}

	private void initEditSave() {
		editSaveButton.setOnAction((e) -> {
			if (writeTextField.getText().equals(NEW)) {
				wb.createAlert();
			} else if (SAVE.equals(editSaveButton.getText())) {
				// Save - New Edit
				T item = choiceBox.getSelectionModel().getSelectedItem();
				if (isNew(item)) {
					setStringFuntion.accept(item, writeTextField.getText());
					saveFunction.accept(item);
				} else {
					setStringFuntion.accept(item, writeTextField.getText());
					editFunction.accept(item);
				}
			} else {
				prepareNewStatus();
			}
		});
	}

	private boolean isNew(T item) {
		return NEW.equals(getStringFunction.apply(item));
	}

	public T deleteSelectedItem() {
		T deletedItem = choiceBox.getSelectionModel().getSelectedItem();
		ObservableList<T> itemList = choiceBox.getItems();
		itemList.remove(deletedItem);
		choiceBox.getSelectionModel().selectFirst();
		return choiceBox.getValue();
	}

	public void addNewItem(List<T> clonedItems) {
		T newItem = newItemSupplier.get();
		setStringFuntion.accept(newItem, NEW);
		clonedItems.add(newItem);
	}

	public void initializeItem(Supplier<T> newItemSupplier, Function<T, String> getStringFunction,
			BiConsumer<T, String> setStringFuntion, BiFunction<T, T, Boolean> equalsFuntion) {
		this.newItemSupplier = newItemSupplier;
		this.setStringFuntion = setStringFuntion;
		this.getStringFunction = getStringFunction;
		this.equalsFuntion = equalsFuntion;
		choiceBox.setConverter(createDefaultConverter(getStringFunction));
	}

	public void initializeModel(Consumer<T> seletectFunction, Consumer<T> saveFunction, Consumer<T> editFunction,
			Consumer<T> deleteFunction) {
		this.seletectFunction = seletectFunction;
		this.saveFunction = saveFunction;
		this.editFunction = editFunction;
		this.deleteFunction = deleteFunction;
	}

	private StringConverter<T> createDefaultConverter(Function<T, String> getStringFunction) {
		return new StringConverter<T>() {

			@Override
			public String toString(T object) {
				return getStringFunction.apply(object);
			}

			@Override
			public T fromString(String string) {
				return choiceBox.getSelectionModel().getSelectedItem();
			}
		};
	}

	private void prepareNewStatus() {
		editSaveButton.setText(SAVE);
		deleteBackButton.setText(BACK);
		writeTextField.setText(getStringFunction.apply(choiceBox.getSelectionModel().getSelectedItem()));
		choiceBoxAnchor.getChildren().setAll(writeTextField);
	}

	public void restoreStatus(T selectedItem) {
		editSaveButton.setText(EDIT);
		deleteBackButton.setText(DELETE);
		choiceBox.getSelectionModel().select(getSelectedItemById(selectedItem, choiceBox.getItems()));
		choiceBox.getItems().add(0, choiceBox.getItems().remove(0)); // Needed to update UI
		choiceBoxAnchor.getChildren().setAll(choiceBox);
	}

	public void updateStatus(T selectedItem, List<T> items) {
		List<T> clonedItems = new ArrayList<>(items);
		this.oldValue = getSelectedItemById(selectedItem, clonedItems);
		SingleSelectionModel<T> selModel=choiceBox.getSelectionModel();
		List<T> choiceBoxItems=choiceBox.getItems();
		selModel.selectedIndexProperty().removeListener(this::changeListener);
		choiceBoxItems.clear();
		addNewItem(clonedItems);
		choiceBoxItems.addAll(clonedItems);
		selModel.selectedIndexProperty().addListener(this::changeListener);
		restoreStatus(selectedItem);
		
	}

	private T getSelectedItemById(T selectedItem, List<T> items) {
		for (T t : items) {
			if (equalsFuntion.apply(t, selectedItem)) {
				oldValue = t;
				return t;
			}
		}
		return null;
	}
}
