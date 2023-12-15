package com.example.proyecto_12_09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner asignaturasGuardadas;  //variable spinner
    EditText ingresoRamos,n1,n2,n3;   // texto de ingreso para ramos
    ArrayList<String> listaDeAsignaturas = new ArrayList<>();  // declarar lista

    Map<String, List<Double>> asignaturasConNotas = new HashMap<>();  // Mapa para almacenar asignaturas con sus notas

    Button btn1,btn2,btn3;   // botones de guardado

    ArrayAdapter<String> adaptador;   // puente entre informacion y la vista

    TextView cajaResultado,cajaStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingresoRamos=(EditText) findViewById(R.id.asignatura);  // declarar variable y de donde la onteome

        btn1=(Button) findViewById(R.id.guardarAsignatura);
        btn2=(Button) findViewById(R.id.guardarNotas);
        btn3=(Button) findViewById(R.id.calcularPromedio);
        n1=(EditText) findViewById(R.id.nota1);
        n2=(EditText) findViewById(R.id.nota2);
        n3=(EditText) findViewById(R.id.nota3);
        cajaResultado=(TextView) findViewById(R.id.resultado);
        cajaStatus= (TextView) findViewById(R.id.status);



        asignaturasGuardadas = findViewById(R.id.asignaturasGuardadas);
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaDeAsignaturas); // adaptador entre la lista de la lista y el
        //spinner
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // funcion de spinner
        asignaturasGuardadas.setAdapter(adaptador);

        if (listaDeAsignaturas.isEmpty()) {
            // Agrega el mensaje de "No hay asignaturas almacenadas" a la lista
            listaDeAsignaturas.add(getString(R.string.mensaje_no_asignaturas));

        }

        asignaturasGuardadas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Cuando se selecciona una asignatura, carga y muestra las notas correspondientes
                String asignaturaSeleccionada = listaDeAsignaturas.get(i);
                List<Double> notas = asignaturasConNotas.get(asignaturaSeleccionada); // lista de notas que
                // se almacena como diccionario clave/valor con el nombre de la nota

                if (notas != null && notas.size() >= 3) {  // if para obtener las notas de una asignatura
                    // Mostrar las notas en los campos de texto
                    n1.setText(String.valueOf(notas.get(0)));
                    n2.setText(String.valueOf(notas.get(1)));
                    n3.setText(String.valueOf(notas.get(2)));
                    cajaStatus.setBackgroundColor(getColor(R.color.white));
                    cajaStatus.setTextColor(getColor(R.color.white));
                    cajaResultado.setText("");




                } else {
                    // Si no hay suficientes notas, limpia los campos de texto y las deja en 0vgfdsg
                    n1.setText("");
                    n2.setText("");
                    n3.setText("");
                    cajaResultado.setText("");
                    cajaStatus.setText("");
                    cajaStatus.setBackgroundColor(getColor(R.color.white));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //funcion de guardado de asignaturas con el primer boon
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strRamo = ingresoRamos.getText().toString();

                if (listaDeAsignaturas.contains(getString(R.string.mensaje_no_asignaturas))) {
                    listaDeAsignaturas.remove(getString(R.string.mensaje_no_asignaturas));
                }

                if (!strRamo.isEmpty()) {
                    listaDeAsignaturas.add(strRamo);  // Añade el nombre de la asignatura a la lista
                    adaptador.notifyDataSetChanged(); //
                    ingresoRamos.setText(""); // Limpia el EditText después de agregar el elemento

                    // Asocia la asignatura con una lista vacía de notas en el mapa si aún no existe
                    if (!asignaturasConNotas.containsKey(strRamo)) {
                        asignaturasConNotas.put(strRamo, new ArrayList<>());
                        Toast.makeText(MainActivity.this, "La asignatura ha sido almacenada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // revisa si hay un item en el spinner
                if (asignaturasGuardadas.getSelectedItem() != null) {
                    String asignaturaSeleccionada = asignaturasGuardadas.getSelectedItem().toString();
                    String A = n1.getText().toString();
                    String B = n2.getText().toString();
                    String C = n3.getText().toString();

                    // Revisa si algun campo esta vacio
                    if (A.isEmpty() || B.isEmpty() || C.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Faltan notas en uno o más campos", Toast.LENGTH_SHORT).show();
                        return; // en caso de que al presionar el boton y no hay notas, se cancela la operacion
                    }

                    // Check if the subject has been previously saved
                    if (!listaDeAsignaturas.contains(asignaturaSeleccionada)) {
                        Toast.makeText(MainActivity.this, "Primero debe guardar la asignatura", Toast.LENGTH_SHORT).show();
                        return; //se sale del metodo si no hay asignatura guardada
                    }

                    try {
                        double numA = Double.parseDouble(A);
                        double numB = Double.parseDouble(B);
                        double numC = Double.parseDouble(C);

                       // revisa si hay notas menores a 1 y mayor a 7
                        if (numA >= 1.0 && numA <= 7.0 &&
                                numB >= 1.0 && numB <= 7.0 &&
                                numC >= 1.0 && numC <= 7.0) {

                            List<Double> notas = asignaturasConNotas.get(asignaturaSeleccionada);
                            if (notas != null) {
                              // al subir las notras quedan registradas, en caso de actualizacion borra las notas de la lista y las sube nuevamente
                                notas.clear(); // para eliminar
                                notas.add(numA);  // agrega notas
                                notas.add(numB);
                                notas.add(numC);

                                cajaResultado.setText(""); // deja en blanco el campo de la caja de resultado
                                cajaStatus.setText("");
                                cajaStatus.setBackgroundColor(getColor(R.color.white));

                                Toast.makeText(MainActivity.this, "Notas actualizadas para " + asignaturaSeleccionada, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "La asignatura seleccionada no existe en la lista.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Los números deben estar entre 1 y 7, use coma para decimales", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Ingrese números válidos en los campos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Seleccione una asignatura antes de guardar notas", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if an item is selected in the Spinner
                if (asignaturasGuardadas.getSelectedItem() != null) {
                    String asignaturaSeleccionada = asignaturasGuardadas.getSelectedItem().toString();
                    String A = n1.getText().toString();
                    String B = n2.getText().toString();
                    String C = n3.getText().toString();

                    if (A.isEmpty() || B.isEmpty() || C.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Faltan notas para calcular el promedio", Toast.LENGTH_SHORT).show();
                        return; // sale de la funcion en caso de no cumplir con lo requerido
                    }

                    try {
                        double numA = Double.parseDouble(A);
                        double numB = Double.parseDouble(B);
                        double numC = Double.parseDouble(C);

                        if (numA >= 1.0 && numA <= 7.0 &&
                                numB >= 1.0 && numB <= 7.0 &&
                                numC >= 1.0 && numC <= 7.0) {

                            double suma = numA + numB + numC;
                            double resultado = suma / 3;

                            // Agrega la nota al mapa asociado con la asignatura seleccionada
                            List<Double> notas = asignaturasConNotas.get(asignaturaSeleccionada);
                            if (notas != null) {
                                notas.add(numA);  // añade la nota
                                notas.add(numB);
                                notas.add(numC);

                                DecimalFormat formato = new DecimalFormat("#.##");
                                String resultadoFormateado = formato.format(resultado);
                                resultado = Double.parseDouble(resultadoFormateado);
                                notas.add(resultado);

                                cajaResultado.setText(resultadoFormateado);

                                if (resultado >= 3.95) {
                                    cajaStatus.setText("Aprobado");
                                    Toast.makeText(MainActivity.this, "Felicitaciones has aprobado", Toast.LENGTH_SHORT).show();
                                    cajaStatus.setBackgroundColor(getColor(R.color.green));
                                    cajaStatus.setTextColor(getColor(R.color.white));

                                } else if (resultado < 3.95) {
                                    cajaStatus.setText("Reprobado");
                                    Toast.makeText(MainActivity.this, "Has reprobado, lo lamentamos", Toast.LENGTH_SHORT).show();
                                    cajaStatus.setBackgroundColor(getColor(R.color.red));
                                    cajaStatus.setTextColor(getColor(R.color.white));
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "La asignatura seleccionada no existe en la lista.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Las notas deben estar entre 1 y 7 y utilizar coma para decimales", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Ingrese números válidos en los campos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Ingrese y guarde una asignatura antes de calcular el promedio", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    }
