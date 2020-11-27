package com.daute.proyectofinaldaute.ui.home;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daute.proyectofinaldaute.MySingleton;
import com.daute.proyectofinaldaute.R;
import com.daute.proyectofinaldaute.Setting_VAR;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GuardarCategoria extends AppCompatActivity implements View.OnClickListener {

    private Button cancel, btnSave;
    private Spinner spEstado;
    private EditText edtId, edtNombre;

    String datoSelect = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Guardar Catagoria");
        setContentView(R.layout.activity_guardar_categoria);
        edtId = findViewById(R.id.edtCategoria);
        edtNombre = findViewById(R.id.edtNombreCategoria);
        spEstado = findViewById(R.id.sp_estado);
        btnSave = findViewById(R.id.btnGuardarCate);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.estadoCategorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEstado.setAdapter(adapter);

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spEstado.getSelectedItemPosition()>0){
                    datoSelect = spEstado.getSelectedItem().toString();
                } else {
                    datoSelect = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGuardarCate:
                String id = edtId.getText().toString();
                String nombre = edtNombre.getText().toString();
                if (id.length() == 0){
                    edtId.setError("Por favor introduzca el Id");
                } else if (nombre.length() == 0){
                    edtNombre.setError("Por favor escriba el nombre de la categoria");
                } else if (spEstado.getSelectedItemPosition() > 0){
                    //this action save in the BD
                    save_server(getApplicationContext(), Integer.parseInt(id), nombre, Integer.parseInt(datoSelect));

                } else {
                    Toast.makeText(getApplicationContext(), "Seleccione un estado para la categoria", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //metodo encargado de gestionar la comunicacion con el servidor y guardar en la base de datos MySql
    private void save_server(final Context context, final int idCat, final String nombreCat, final int estadoCat){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.URL_GUARDAR_CATEGORIAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    if (estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();

                    } else if (estado.equals("2")){
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
                map.put("id", String.valueOf(idCat));
                map.put("nombre", nombreCat);
                map.put("estado", String.valueOf(estadoCat));
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