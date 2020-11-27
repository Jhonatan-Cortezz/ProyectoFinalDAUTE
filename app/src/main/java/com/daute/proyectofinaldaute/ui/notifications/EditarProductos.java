package com.daute.proyectofinaldaute.ui.notifications;

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
import com.daute.proyectofinaldaute.ui.home.DtoCategoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarProductos extends AppCompatActivity {
    private EditText edtId, edtNombreProd, edtDescripcionProd, edtStock, edtPrecio, edtUnidadMedida;
    private Spinner spEstadoProducto, spFkCategoria;
    private TextView tvFecha;
    private Button btnEditar;

    ArrayList<String> lista = null;
    ArrayList<DtoCategoria> listaCategoria;//para el spinnner

    String idCategoria = "";
    String nombreCategoria = "";
    int conta = 0;

    String datoStatusProduct = "";

    DtoProductos dto = new DtoProductos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Editar productos");
        setContentView(R.layout.activity_editar_productos);
        edtId = findViewById(R.id.edtIdProUp);
        edtNombreProd = findViewById(R.id.edtNombreProductoUp);
        edtDescripcionProd = findViewById(R.id.edtDescripcionUp);
        edtStock = findViewById(R.id.edtStockUp);
        edtPrecio = findViewById(R.id.edtPrecioUp);
        edtUnidadMedida = findViewById(R.id.edtUnidadDeMedidadUp);
        spEstadoProducto = findViewById(R.id.spEstadoProductoUp);
        spFkCategoria = findViewById(R.id.spFkCategoriaUp);
        tvFecha = findViewById(R.id.tvFecha);
        tvFecha.setText(timedate());

        btnEditar = findViewById(R.id.btnSaveProdUp);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.estadoProducto, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEstadoProducto.setAdapter(adapter);

        spEstadoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spEstadoProducto.getSelectedItemPosition()>0){
                    datoStatusProduct = spEstadoProducto.getSelectedItem().toString();
                } else {
                    datoStatusProduct = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fk_categoria(getApplicationContext());

        spFkCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (conta>=1 && spFkCategoria.getSelectedItemPosition()>0){
                    String itemSpinner = spFkCategoria.getSelectedItem().toString();

                    String s[] = itemSpinner.split("~");

                    idCategoria = s[0].trim();
                    nombreCategoria = s[1];
                    //Toast.makeText(getApplicationContext(), "Id cat: " + idCategoria + "\n" + "Nombre categoria: " + nombreCategoria, Toast.LENGTH_SHORT).show();

                } else {
                    idCategoria = "";
                    nombreCategoria = "";
                }
                conta++;
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

                if (senal.equals("1")){
                    edtId.setText(codigo);
                    edtNombreProd.setText(nombre);
                    edtDescripcionProd.setText(apellido);
                    edtStock.setText(correo);
                    edtPrecio.setText(usuario);
                    edtUnidadMedida.setText(clave);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtId.getText().toString();
                String nombre = edtNombreProd.getText().toString();
                String apellido = edtDescripcionProd.getText().toString();
                String correo = edtStock.getText().toString();
                String usuario = edtPrecio.getText().toString();
                String clave = edtUnidadMedida.getText().toString();
                if (id.length() == 0){
                    edtId.setError("Por favor introduzca el Id");
                } else {
                    update_server(getApplicationContext(), id, nombre, apellido, correo, usuario, clave, Integer.parseInt(datoStatusProduct), Integer.parseInt(idCategoria));
                }
            }
        });
    }

    private void update_server(final Context context, final String idUsu, final String nombreU, final String apellido, final String correo, final String usuario, final String clave, final int tipo, final int estado){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.URL_UPDATE_PRODUCTO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estadoJson = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    if (estadoJson.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarProductos.this, MainActivity.class);
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

    public void fk_categoria(final Context context){
        listaCategoria = new ArrayList<DtoCategoria>();
        lista = new ArrayList<>();
        lista.add("Seleccione categoria");

        String url = Setting_VAR.URL_SPINNER_CAT;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    int totalEncontrados = array.length();

                    DtoCategoria objCategoria = null;

                    for (int i = 0; i < array.length(); i++){
                        JSONObject categoriasObject = array.getJSONObject(i);
                        int id_categoria = categoriasObject.getInt("id_categoria");
                        String nombre_categoria = categoriasObject.getString("nom_categoria");
                        String estado_categoria =categoriasObject.getString("estado_categoria");

                        objCategoria = new DtoCategoria(id_categoria, nombre_categoria, Integer.parseInt(estado_categoria));

                        listaCategoria.add(objCategoria);

                        //view info
                        lista.add(listaCategoria.get(i).getIdCategoria() + " ~ " + listaCategoria.get(i).getNombre());

                        //adaptador
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
                        spFkCategoria.setAdapter(adaptador);
                    }

                } catch (JSONException ex){
                    String loli = ex.toString();
                    Log.i("Error ??????", loli);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Error. Compruebe su acceso a Internet.", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private String timedate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String fecha = sdate.format(cal.getTime());
        return fecha;
    }
}