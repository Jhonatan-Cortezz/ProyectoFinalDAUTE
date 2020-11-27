package com.daute.proyectofinaldaute.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class DetalleUsuario extends AppCompatActivity implements View.OnClickListener {
    private TextView tvCodigo, tvNombre, tvApellido, tvCorreo, tvUsuario, tvClave, tvTipo, tvEstado, tvPregunta, tvRespuesta;
    private Button btnEditarUse, btnBorrarUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detalle de Registro");
        setContentView(R.layout.activity_detalle_usuario);
        tvCodigo = findViewById(R.id.tvCodigoUsuario);
        tvNombre = findViewById(R.id.tvNombreUsuario);
        tvApellido = findViewById(R.id.tvApellido);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvUsuario = findViewById(R.id.tvUsuario);
        tvClave = findViewById(R.id.tvClave);
        tvTipo = findViewById(R.id.tvTipo);
        tvEstado = findViewById(R.id.tvEstado);
        tvPregunta = findViewById(R.id.tvPregunta);
        tvRespuesta = findViewById(R.id.tvRespuesta);
        btnEditarUse = findViewById(R.id.btnEditarUse);
        btnBorrarUse = findViewById(R.id.btnBorrarUse);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("codigo");
        String nombre = bundle.getString("nombre");
        String apellido  = bundle.getString("apellido");
        String correo  = bundle.getString("correo");
        String usuario  = bundle.getString("usuario");
        String clave  = bundle.getString("clave");
        String tipo  = bundle.getString("tipo");
        String estado  = bundle.getString("estado");
        String pregunta  = bundle.getString("pregunta");
        String respuesta  = bundle.getString("respuesta");

        tvCodigo.setText(id);
        tvNombre.setText(nombre);
        tvApellido.setText(apellido);
        tvCorreo.setText(correo);
        tvUsuario.setText(usuario);
        tvClave.setText(clave);
        tvTipo.setText(tipo);
        tvEstado.setText(estado);
        tvPregunta.setText(pregunta);
        tvRespuesta.setText(respuesta);

        btnEditarUse.setOnClickListener(this);
        btnBorrarUse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String id = tvCodigo.getText().toString();
        if (view.getId() == R.id.btnBorrarUse){
            delete_use(this, Integer.parseInt(id));

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnEditarUse){
            String codigo = "" + tvCodigo.getText().toString();
            String nombre = "" + tvNombre.getText().toString();
            String apellido = "" + tvApellido.getText().toString();
            String correo = "" + tvCorreo.getText().toString();
            String usuario = "" + tvUsuario.getText().toString();
            String clave = "" + tvClave.getText().toString();
            String pregunta = "" + tvPregunta.getText().toString();
            String respuesta = "" + tvRespuesta.getText().toString();

            Intent intent = new Intent(getApplicationContext(), EditarUsuario.class);
            intent.putExtra("senal", "1");
            intent.putExtra("codigo", codigo);
            intent.putExtra("nombre", nombre);
            intent.putExtra("apellido", apellido);
            intent.putExtra("correo", correo);
            intent.putExtra("usuario", usuario);
            intent.putExtra("clave", clave);
            intent.putExtra("pregunta", pregunta);
            intent.putExtra("respuesta", respuesta);
            startActivity(intent);
        }
    }

    private void delete_use(final Context context, final int idCat){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.URL_DELETE_USUARIO, new Response.Listener<String>() {
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
                Toast.makeText(context, "Error No hay registro", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", String.valueOf(idCat));
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