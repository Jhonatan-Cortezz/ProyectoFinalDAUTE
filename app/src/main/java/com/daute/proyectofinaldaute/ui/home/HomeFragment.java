package com.daute.proyectofinaldaute.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.daute.proyectofinaldaute.MySingleton;
import com.daute.proyectofinaldaute.R;
import com.daute.proyectofinaldaute.Setting_VAR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;

//this class is a Entity for Table categoria
public class HomeFragment extends Fragment {
    private Button btnNew;
    ArrayList<String> lista = null;
    private LinearLayoutCompat resultado;
    private ListView lst;
    ArrayList<DtoCategoria> listaCategoria;
    String datoSelect = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final LinearLayoutCompat frameLayout2 = root.findViewById(R.id.fm2);
        btnNew = root.findViewById(R.id.btnNuevo);
        resultado = root.findViewById(R.id.fm2);
        lst = root.findViewById(R.id.lstCategoria);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnNew.isClickable() == true) {
                    Intent intent = new Intent(getContext(), GuardarCategoria.class);
                    startActivity(intent);
                }
            }
        });

        recibirAllCat();

        return root;
    }

    private void recibirAllCat(){
        listaCategoria = new ArrayList<DtoCategoria>();
        lista = new ArrayList<String>();
        String urlConsultaCategoria = Setting_VAR.URL_CONSULTAR_CATEGORIAS;
        StringRequest request = new StringRequest(Request.Method.POST, urlConsultaCategoria, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respuestaJSOn = new JSONArray(response);
                    int totalEnct = respuestaJSOn.length();

                    DtoCategoria objCategorias = null;
                    for (int i = 0; i < respuestaJSOn.length(); i++){
                        JSONObject categoriaObj = respuestaJSOn.getJSONObject(i);
                        int idC = categoriaObj.getInt("id_categoria");
                        String name = categoriaObj.getString("nom_categoria");
                        int stado = categoriaObj.getInt("estado_categoria");

                        objCategorias = new DtoCategoria(idC, name, stado);

                        listaCategoria.add(objCategorias);

                        lista.add(listaCategoria.get(i).getIdCategoria() + " - " + listaCategoria.get(i).getNombre());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lista);
                        lst.setAdapter(adapter);

                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String codigo = "" + listaCategoria.get(i).getIdCategoria();
                                String nombre = "" + listaCategoria.get(i).getNombre();
                                String estado = "" + listaCategoria.get(i).getEstado();


                                Activity activity = new Activity();
                                Intent intent = new Intent(getActivity(), DetalleCategoria.class);
                                intent.putExtra("codigo", codigo);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("estado", estado);
                                startActivity(intent);
                            }
                        });

                        Log.i("Id Categoria:    ", String.valueOf(objCategorias.getIdCategoria()));
                        Log.i("Nombre:    ", String.valueOf(objCategorias.getNombre()));

                    }
                    //resultado.setText("Datos: " + response.toString());
                    //Toast.makeText(getContext(), "Id: " + idCategori + "\nNombre: " + nombreCat + "\nEstado: " + estadoCat, Toast.LENGTH_SHORT).show();

                } catch (JSONException ex){
                    String none = ex.toString();
                    Log.i("NO consulta ***** ", none);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String err = volleyError.toString();
                Log.i("No se pudo **********", err);
            }
        });
        //tiempo de respuesta, establece politica de reintentos
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}