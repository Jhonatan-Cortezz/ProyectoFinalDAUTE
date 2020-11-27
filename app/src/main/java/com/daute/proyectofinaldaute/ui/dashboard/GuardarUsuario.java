package com.daute.proyectofinaldaute.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daute.proyectofinaldaute.MySingleton;
import com.daute.proyectofinaldaute.R;
import com.daute.proyectofinaldaute.Setting_VAR;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GuardarUsuario extends AppCompatActivity {
    private EditText edtId, edtNombreUse, edtApellido, edtCorreo, edtUsuario, edtClave, edtPregunta, edtRespuesta;
    private Spinner spTipo, spEstadoU;
    private TextView tvFecha;
    private Button btnSave, btnNew;

    String estado = "";
    String tipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Registrar Usuario");
        setContentView(R.layout.activity_guardar_usuario);
        edtId = findViewById(R.id.edtIdUs);
        edtNombreUse = findViewById(R.id.edtNombreUse);
        edtApellido = findViewById(R.id.edtApellido);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtUsuario = findViewById(R.id.edtUsuario);
        edtClave = findViewById(R.id.edtClave);
        spTipo = findViewById(R.id.spTipo);
        edtPregunta = findViewById(R.id.edtPregunta);
        edtRespuesta = findViewById(R.id.edtRespuesta);
        spEstadoU = findViewById(R.id.spEstadoUse);
        tvFecha = findViewById(R.id.tvFechaR);
        tvFecha.setText(timedate());

        ArrayAdapter<CharSequence> adapterSt = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.estadoUsuario, android.R.layout.simple_spinner_item);
        adapterSt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterTipe = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.tipoUsuario, android.R.layout.simple_spinner_item);
        adapterTipe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEstadoU.setAdapter(adapterSt);
        spTipo.setAdapter(adapterTipe);

        spEstadoU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spEstadoU.getSelectedItemPosition()>0){
                    estado = spEstadoU.getSelectedItem().toString();
                } else {
                    estado = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spTipo.getSelectedItemPosition()>0){
                    tipo = spTipo.getSelectedItem().toString();
                } else {
                    tipo = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave = findViewById(R.id.btnSaveUse);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtId.getText().toString();
                String nombre = edtNombreUse.getText().toString();
                String descripcion = edtApellido.getText().toString();
                String stock = edtCorreo.getText().toString();
                String precio = edtUsuario.getText().toString();
                String unidad = edtClave.getText().toString();
                String quest = edtPregunta.getText().toString();
                String respons = edtRespuesta.getText().toString();

                if (id.length() == 0){
                    edtId.setError("Campo obligatirio");
                } else if (nombre.length() == 0){
                    edtNombreUse.setError("Campo obligatorio");
                } else if (descripcion.length() == 0){
                    edtApellido.setError("Campo obligatorio");
                } else if (stock.length() == 0){
                    edtCorreo.setError("Campo obligatorio");
                } else if (precio.length() == 0){
                    edtUsuario.setError("Campo obligatorio");
                } else if (unidad.length() == 0){
                    edtClave.setError("Campo obligatorio");
                } else if (spEstadoU.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar el estado de producto", Toast.LENGTH_SHORT).show();
                } else if (spTipo.getSelectedItemPosition() > 0){
                    save_productos(getApplicationContext(), id, nombre, descripcion, stock, precio, unidad, Integer.parseInt(estado), Integer.parseInt(tipo), quest, respons);
                } else {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar la categoria", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String timedate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String fecha = sdate.format(cal.getTime());
        return fecha;
    }

    private void save_productos(final Context context, final String idUsu, final String nombre, final String apellido, final String correo, final String usuario, final String clave, final int tipo, final int estado, final String pregunta, final String respuesta){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Setting_VAR.URL_GUARDAR_USUARIO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJson = null;
                try {
                    requestJson = new JSONObject(response.toString());
                    String estado = requestJson.getString("estado");
                    String mensaje = requestJson.getString("mensaje");

                    if (estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    } else if (estado.equals("2")){
                        Toast.makeText(context, ""+mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex){
                    String lol = ex.toString();
                    Log.i("Error **************", lol);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se puedo guardar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", idUsu);
                map.put("nombre", nombre);
                map.put("apellido", apellido);
                map.put("correo", correo);
                map.put("usuario", usuario);
                map.put("clave", clave);
                map.put("tipo", String.valueOf(tipo));
                map.put("estado", String.valueOf(estado));
                map.put("pregunta", pregunta);
                map.put("respuesta", respuesta);
                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}