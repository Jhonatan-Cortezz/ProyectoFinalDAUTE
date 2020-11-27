package com.daute.proyectofinaldaute.ui.notifications;

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
import com.daute.proyectofinaldaute.ui.home.DtoCategoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GuardarProductos extends AppCompatActivity {
    private EditText edtId, edtNombreProd, edtDescripcionProd, edtStock, edtPrecio, edtUnidadMedida;
    private Spinner spEstadoProducto, spFkCategoria;
    private TextView tvFecha;
    private Button btnSave, btnNew;

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
        setTitle("Guardar Producto");
        setContentView(R.layout.activity_guardar_productos);
        edtId = findViewById(R.id.edtId);
        edtNombreProd = findViewById(R.id.edtNombreProducto);
        edtDescripcionProd = findViewById(R.id.edtDescripcion);
        edtStock = findViewById(R.id.edtStock);
        edtPrecio = findViewById(R.id.edtPrecio);
        edtUnidadMedida = findViewById(R.id.edtUnidadDeMedidad);
        spEstadoProducto = findViewById(R.id.spEstadoProducto);
        spFkCategoria = findViewById(R.id.spFkCategoria);
        tvFecha = findViewById(R.id.tvFecha);
        tvFecha.setText(timedate());

        btnSave = findViewById(R.id.btnSaveProd);

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtId.getText().toString();
                String nombre = edtNombreProd.getText().toString();
                String descripcion = edtDescripcionProd.getText().toString();
                String stock = edtStock.getText().toString();
                String precio = edtPrecio.getText().toString();
                String unidad = edtUnidadMedida.getText().toString();

                if (id.length() == 0){
                    edtId.setError("Campo obligatirio");
                } else if (nombre.length() == 0){
                    edtNombreProd.setError("Campo obligatorio");
                } else if (descripcion.length() == 0){
                    edtDescripcionProd.setError("Campo obligatorio");
                } else if (stock.length() == 0){
                    edtStock.setError("Campo obligatorio");
                } else if (precio.length() == 0){
                    edtPrecio.setError("Campo obligatorio");
                } else if (unidad.length() == 0){
                    edtUnidadMedida.setError("Campo obligatorio");
                } else if (spEstadoProducto.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar el estado de producto", Toast.LENGTH_SHORT).show();
                } else if (spFkCategoria.getSelectedItemPosition() > 0){
                    save_productos(getApplicationContext(), id, nombre, descripcion, stock, precio, unidad, datoStatusProduct, idCategoria);
                } else {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar la categoria", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_product();
            }
        });*/

    }

    private String timedate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String fecha = sdate.format(cal.getTime());
        return fecha;
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

    private void save_productos(final Context context, final String idProducto, final String nombreProd, final String desripProd, final String stock, final String precio, final String unidadMed, final String estadoProd, final String categoria){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Setting_VAR.URL_REGISTRAR_PRODUCTOS, new Response.Listener<String>() {
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
                map.put("id_prod", idProducto);
                map.put("nom_prod", nombreProd);
                map.put("des_prod", desripProd);
                map.put("stock", stock);
                map.put("precio", precio);
                map.put("uni_medida", unidadMed);
                map.put("estado_prod", estadoProd);
                map.put("categoria", categoria);

                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void new_product(){
        edtId.setText(null);
        edtNombreProd.setText(null);
        edtDescripcionProd.setText(null);
        edtPrecio.setText(null);
        edtStock.setText(null);
        edtUnidadMedida.setText(null);
        spEstadoProducto.setSelection(0);
        spEstadoProducto.setSelection(0);
    }
}