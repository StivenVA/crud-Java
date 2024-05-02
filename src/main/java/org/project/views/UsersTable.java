package org.project.views;

import org.project.dto.EmployeeDTO;
import org.project.fachada.ApplicationFachada;
import org.project.interfaces.observer.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsersTable extends JFrame implements Observer {
    private JPanel panel1;
    private JTable users;
    private final ApplicationFachada applicationFachada = ApplicationFachada.getInstance();

    public UsersTable(){
        setContentPane(panel1);
        users.setModel(setData());
        users.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500); // Intervalo de actualización (5 segundos)
                    updateTable();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private DefaultTableModel setData(){
        return new DefaultTableModel(
                applicationFachada.findAll().stream().map(employeeDTO -> new Object[]{
                        employeeDTO.getId(),
                        employeeDTO.getName(),
                        employeeDTO.getLastName(),
                        employeeDTO.getBirthdate(),
                        employeeDTO.getDirection(),
                        employeeDTO.getPhone(),
                        employeeDTO.getEmail()
                }).toArray(Object[][]::new),
                new String[]{"ID", "Name", "Last Name", "Birthdate", "Direction", "Phone", "Email"}
        );
    }

    @Override
    public void notify(EmployeeDTO employeeDTO) {

        int index = indexOfEmployee(employeeDTO);

        if (employeeDTO.getName() != null){
            if (index == -1)
                addRow(employeeDTO);
            else{
                DefaultTableModel model = (DefaultTableModel) users.getModel();
                model.setValueAt(employeeDTO.getName(), index, 1);
                model.setValueAt(employeeDTO.getLastName(), index, 2);
                model.setValueAt(employeeDTO.getBirthdate(), index, 3);
                model.setValueAt(employeeDTO.getDirection(), index, 4);
                model.setValueAt(employeeDTO.getPhone(), index, 5);
                model.setValueAt(employeeDTO.getEmail(), index, 6);
            }
        }
        else {
            deleteRow(index);
        }
    }

    public int indexOfEmployee(EmployeeDTO employeeDTO){
        DefaultTableModel model = (DefaultTableModel) users.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(employeeDTO.getId())) {

                return i;
            }
        }
        return -1;
    }

    public void deleteRow(int i){
        if (i == -1) return;

        DefaultTableModel model = (DefaultTableModel) users.getModel();
        model.removeRow(i);
    }

    public void addRow(EmployeeDTO employeeDTO){
        DefaultTableModel model = (DefaultTableModel) users.getModel();
        model.addRow(new Object[]{
                employeeDTO.getId(),
                employeeDTO.getName(),
                employeeDTO.getLastName(),
                employeeDTO.getBirthdate(),
                employeeDTO.getDirection(),
                employeeDTO.getPhone(),
                employeeDTO.getEmail()
        });
    }

    public void updateTable() {
        // Actualiza los datos de la tabla
        DefaultTableModel model = (DefaultTableModel) users.getModel();
         // Borra los datos existentes en la tabla
        List<EmployeeDTO> employees = applicationFachada.findAll();

        if (employees.size()<model.getRowCount() || employees.size()>model.getRowCount()){
            model.setRowCount(0);
            users.setModel(setData());
        }
        else {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(employees.get(i).getId())) {
                    model.setValueAt(employees.get(i).getName(), i, 1);
                    model.setValueAt(employees.get(i).getLastName(), i, 2);
                    model.setValueAt(employees.get(i).getBirthdate(), i, 3);
                    model.setValueAt(employees.get(i).getDirection(), i, 4);
                    model.setValueAt(employees.get(i).getPhone(), i, 5);
                    model.setValueAt(employees.get(i).getEmail(), i, 6);
                }
            }
        }
    }

    public void iniciar(){
        setTitle("Welcome");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
