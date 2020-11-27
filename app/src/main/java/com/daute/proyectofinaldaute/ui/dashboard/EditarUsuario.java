package com.daute.proyectofinaldaute.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daute.proyectofinaldaute.MainActivity;
import com.daute.proyectofinaldaute.MySingleton;
import com.daute.proyectofinaldaute.R;
import com.daute.proyectofinaldaute.Setting_VAR;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditarUsuario extends AppCompatActivity {
    private EditText edtId, edtNombreUse, edtApellido, edtCorreo, edtUsuario, edtClave, edtPregunta, edtRespuesta;
    private Spinner spTipo, spEstadoU;
    private Button btnUpdate;
    String estado = "";
    String tipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Editar Registro");
        setContentView(R.layout.activity_editar_usuario);
        edtId = findViewById(R.id.edtIdUsUp);
        edtNombreUse = findViewById(R.id.edtNombreUseUp);
        edtApellido = findViewById(R.id.edtApellidoUp);
        edtCorreo = findViewById(R.id.edtCorreoUp);
        edtUsuario = findViewById(R.id.edtUsuarioUp);
        edtClave = findViewById(R.id.edtClaveUp);
        spTipo = findViewById(R.id.spTipoUp);
        edtPregunta = findViewById(R.id.edtPreguntaUp);
        edtRespuesta = findViewById(R.id.edtRespuestaUp);
        spEstadoU = findViewById(R.id.spEstadoUseUp);
        btnUpdate = findViewById(R.id.btnUpdateUser);

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

        String senal = "";
        String codigo = "";
        String nombre = "";
        String apellido = "";
        String correo = "";
        String usuario = "";
        String clave = "";
        String pregunta = "";
        String respuesta = "";

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                codigo = bundle.getString("codigo");
                senal = bundle.getString("senal");
                nombre = bundle.getString("nombre");
                apellido = bundle.getString("apellido");
                correo = bundle.getString("correo");
                usuario = bundle.getString("usuario");
                clave = bundle.getString("clave");
                pregunta = bundle.getString("pregunta");
                respuesta = bundle.getString("respuesta");

                if (senal.equals("1")){
                    edtId.setText(codigo);
                    edtNombreUse.setText(nombre);
                    edtApellido.setText(apellido);
                    edtCorreo.setText(correo);
                    edtUsuario.setText(usuario);
                    edtClave.setText(clave);
                    edtPregunta.setText(pregunta);
                    edtRespuesta.setText(respuesta);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtId.getText().toString();
                String nombre = edtNombreUse.getText().toString();
                String apellido = edtApellido.getText().toString();
                String correo = edtCorreo.getText().toString();
                String usuario = edtUsuario.getText().toString();
                String clave = edtClave.getText().toString();
                String pregunta = edtPregunta.getText().toString();
                String respuesta = edtRespuesta.getText().toString();
                if (id.length() == 0){
                    edtId.setError("Por favor introduzca el Id");
                } else {
                    update_server(getApplicationContext(), id, nombre, apellido, correo, usuario, clave, Integer.parseInt(estado), Integer.parseInt(tipo), pregunta, respuesta);
                }
            }
        });

    }

    private void update_server(final Context context, final String idUsu, final String nombreU, final String apellido, final String correo, final String usuario, final String clave, final int tipo, final int estado, final String pregunta, final String respuesta){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.URL_UPDATE_USUARIO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estadoJson = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    if (estadoJson.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarUsuario.this, MainActivity.class);
                        startActivity(intent);
                    } else if (estadoJson.equals("2")){
                        Toast.makeText(context, ""+mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    String logit = e.toString();
                    Log.i("JsonExceprtion*********", logit);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String bas = volleyError.toString();
                Log.i("No guarda nada ********", bas);
                Toast.makeText(context, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", String.valueOf(idUsu));
                map.put("nombre", nombreU);
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

        //tiempo de respuesta, establece politica de reintentos
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}