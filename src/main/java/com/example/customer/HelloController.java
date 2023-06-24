package com.example.customer;

import com.example.customer.Show.Show_Window;
import com.example.customer.customerInfor.CustomerInformation;
import com.example.customer.database.database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private TableView<CustomerInformation> Customer_Table;

    @FXML
    private TableColumn<CustomerInformation, String> Email;

    @FXML
    private TableColumn<CustomerInformation, String> ID_Khachhang;

    @FXML
    private TableColumn<CustomerInformation, String> Name;

    @FXML
    private TableColumn<CustomerInformation, String> Password;

    @FXML
    private TableColumn<CustomerInformation, String> PhoneNumber;

    @FXML
    private TableColumn<CustomerInformation, String> Username;

    @FXML
    private Button dashbroad_close;
    @FXML
    private TableColumn<CustomerInformation, Button> Delete_Col;
    @FXML
    private TableColumn<CustomerInformation, Button> Update_Col;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CustomerInformation customerInformation = null;

    private static String ID_Customer;

    public static String ID_Customer() {
        return ID_Customer;
    }

    ObservableList<CustomerInformation> CustomerList = FXCollections.observableArrayList();

    @FXML
    void Setdashbroaed_close(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private void loadDate()
    {
        connection = database.connectionDb();
        refreshTable();

        ID_Khachhang.setCellValueFactory(new PropertyValueFactory<>("ID_Khachhang"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        PhoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        Username.setCellValueFactory(new PropertyValueFactory<>("Username"));
        Password.setCellValueFactory(new PropertyValueFactory<>("Password"));
        Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Delete_Col.setCellValueFactory(new PropertyValueFactory<>("Delete_Col"));
        Update_Col.setCellValueFactory(new PropertyValueFactory<>("Update_Col"));

        Delete_Col.setCellFactory(param -> new TableCell<>() {
            Button deleteButton = new Button("Xóa");



            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        CustomerInformation customerInformation = getTableView().getItems().get(getIndex());
                        CustomerList.remove(customerInformation);
                        Delete_inDB(customerInformation.getID_KhachHang());
                    });
                }
            }
        });

        Update_Col.setCellFactory(param -> new TableCell<>() {
            Button inforButton = new Button("Chi tiết");
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(inforButton);

                    inforButton.setOnAction(event -> {
                        CustomerInformation customerInformation = getTableView().getItems().get(getIndex());
                        ID_Customer = customerInformation.getID_KhachHang();
                        String FXMLPATH = "com/example/customer/InformationView/Information_train.fxml";
                        try {
                            Show_Window showWindow =new Show_Window();
                            showWindow.Show(FXMLPATH);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }

    private void refreshTable() {
        CustomerList.clear();

        try {
            query = "SELECT * FROM khach_hang";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                CustomerList.add(new CustomerInformation(
                        resultSet.getString("ID_Khachhang"),
                        resultSet.getString("Name"),
                        resultSet.getString("PhoneNumber"),
//                        resultSet.getString("Username"),
//                        resultSet.getString("Password"),
                        resultSet.getString("Email")));
                        Customer_Table.setItems(CustomerList);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void Delete_inDB(String ID)
    {

        query = "DELETE FROM khach_hang WHERE ID_Khachhang = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,ID);
            int check = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
