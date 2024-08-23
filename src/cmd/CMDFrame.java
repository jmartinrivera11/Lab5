package lab5_programacion2;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class CMDFrame extends JFrame {
    private Funciones cmd = new Funciones("");
    private JTextArea window = new JTextArea();
    private JScrollPane panel = new JScrollPane(window);
    private int lastEditablePosition;

    public CMDFrame() {
        setupFrame();
        window.setText("Microsoft Windows [Version 10.0.22621.521]" 
                + "\n(c) Microsoft Corporation. All rights reserved." + "\n\n" 
                + cmd.getPath() + ">");
        lastEditablePosition = window.getText().length(); 
    }

    private void setupFrame() {
        window.setBackground(Color.black);
        window.setForeground(Color.white);
        window.setFont(new java.awt.Font("Consolas", 0, 16));
        window.setEditable(true);
        getContentPane().setLayout(new BorderLayout());

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    handleEnterPressed();
                } else if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) &&
                        window.getCaretPosition() <= lastEditablePosition) {
                    e.consume();
                } else if (window.getCaretPosition() < lastEditablePosition) {
                    window.setCaretPosition(lastEditablePosition); 
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (window.getCaretPosition() < lastEditablePosition) {
                    e.consume(); 
                }
            }
        });

        setTitle("Administrator: Command Prompt");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void handleEnterPressed() {
        String text = window.getText();
        int lastIndex = text.lastIndexOf("\n" + cmd.getPath() + ">");
        if (lastIndex != -1) {
            String command = text.substring(lastIndex + cmd.getPath().length() + 2).trim();
            executeCommand(command);
        }
    }

    private void executeCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0].toLowerCase()) {
            case "mkdir":
                if (parts.length == 1) {
                    appendOutput("Ingresar el nombre del directorio");
                } else {
                    appendOutput(cmd.mkdir(cmd.getPath() + "/" + parts[1]));
                }
                break;

            case "mfile":
                if (parts.length == 1) {
                    appendOutput("Ingresar el nombre del archivo");
                } else {
                    appendOutput(cmd.mfile(cmd.getPath() + "/" + parts[1]));
                }
                break;

            case "rm":
                if (parts.length == 1) {
                    appendOutput("Ingrese el nombre de carpeta/archivo a eliminar");
                } else {
                    File file = new File(cmd.getPath() + "/" + parts[1]);
                    appendOutput(cmd.eliminar(file));
                }
                break;

            case "cd":
                if (parts.length == 1) {
                    appendOutput("Ingresar el nombre del directorio");
                } else {
                    cmd.cd(parts[1]);
                }
                break;

            case "...":
                cmd.cd("..");
                break;

            case "dir":
                appendOutput(cmd.listar(cmd.getPath()));
                break;

            case "date":
                appendOutput(LocalDate.now().toString());
                break;

            case "time":
                appendOutput(LocalTime.now().toString());
                break;

            case "wr":
                if (parts.length < 3) {
                    appendOutput("Ingrese el nombre del archivo y contenido");
                } else {
                    StringBuilder content = new StringBuilder();
                    for (int i = 2; i < parts.length; i++) {
                        content.append(parts[i]).append(" ");
                    }
                    appendOutput(cmd.escribir(content.toString().trim(), cmd.getPath() + "/" + parts[1]));
                }
                break;

            case "rd":
                if (parts.length == 1) {
                    appendOutput("Ingrese el nombre del archivo a leer");
                } else {
                    appendOutput(cmd.leer(cmd.getPath() + "/" + parts[1]));
                }
                break;

            default:
                appendOutput("Comando no reconocido: " + command);
                break;
        }
        window.append("\n" + cmd.getPath() + ">");
        lastEditablePosition = window.getText().length(); 
    }

    private void appendOutput(String msg) {
        window.append("\n" + msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CMDFrame frame = new CMDFrame();
            frame.setVisible(true);
        });
    }
}
