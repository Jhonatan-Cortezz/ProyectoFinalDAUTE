package com.daute.proyectofinaldaute.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daute.proyectofinaldaute.MySingleton;
import com.daute.proyectofinaldaute.R;
import com.daute.proyectofinaldaute.Setting_VAR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private Button btnNew;
    ArrayList<String> lista = null;
    private LinearLayoutCompat resultado;
    private ListView lst;
    ArrayList<DtoUsuario> listaUsuario;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        btnNew = root.findViewById(R.id.btnNuevoUse);
        resultado = root.findViewById(R.id.fm2U);
        lst = root.findViewById(R.id.lstUsuario);

        recibirAllUse();

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnNew.isClickable() == true) {
                    Intent intent = new Intent(getContext(), GuardarUsuario.class);
                    startActivity(intent);
                }
            }
        });
        return root;
    }

    private void recibirAllUse(){
        listaUsuario = new ArrayList<DtoUsuario>();
        lista = new ArrayList<String>();
        String urlConsultaCategoria = Setting_VAR.URL_LISTA_USUARIO;
        StringRequest request = new StringRequest(Request.Method.POST, urlConsultaCategoria, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respuestaJSOn = new JSONArray(response);
                    int totalEnct = respuestaJSOn.length();

                    DtoUsuario objCategorias = null;
                    for (int i = 0; i < respuestaJSOn.length(); i++){
                        JSONObject categoriaObj = respuestaJSOn.getJSONObject(i);
                        int idC = categoriaObj.getInt("id");
                        String name = categoriaObj.getString("nombre");
                        String apelli = categoriaObj.getString("apellidos");
                        String email = categoriaObj.getString("correo");
                        String use = categoriaObj.getString("usuario");
                        String pass = categoriaObj.getString("clave");
                        int tip = categoriaObj.getInt("tipo");
                        int stado = categoriaObj.getInt("estado");
                        String preg = categoriaObj.getString("pregunta");
                        String rest = categoriaObj.getString("respuesta");

                        objCategorias = new DtoUsuario(idC, name, apelli, email, use, pass, tip, stado, preg, rest);

                        listaUsuario.add(objCategorias);

                        //lista.add(listaCategoria.get(i).getIdCategoria() + " - " + listaCategoria.get(i).getNombre());
                        lista.add(listaUsuario.get(i).getIdUsuario() + " - " + listaUsuario.get(i).getNombreUsuario() + " - " + listaUsuario.get(i).getApellidoUsuario()+ " - " + listaUsuario.get(i).getCorreo() + " - " + listaUsuario.get(i).getUsuario() + " - " + listaUsuario.get(i).getClave() + " - " + listaUsuario.get(i).getTipo() + " - " + listaUsuario.get(i).getEstado() + " - " + listaUsuario.get(i).getPregunta() + " - " + listaUsuario.get(i).getRespuesta());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lista);
                        lst.setAdapter(adapter);

                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String codigo = "" + listaUsuario.get(i).getIdUsuario();
                                String nombre = "" + listaUsuario.get(i).getNombreUsuario();
                                String apellido = "" + listaUsuario.get(i).getApellidoUsuario();
                                String correo = "" + listaUsuario.get(i).getCorreo();
                                String usuario = "" + listaUsuario.get(i).getUsuario();
                                String clave = "" + listaUsuario.get(i).getClave();
                                String tipo = "" + listaUsuario.get(i).getTipo();
                                String estadou = "" + listaUsuario.get(i).getEstado();
                                String pregunta = "" + listaUsuario.get(i).getPregunta();
                                String respuesta = "" + listaUsuario.get(i).getRespuesta();

                                Activity activity = new Activity();
                                Intent intent = new Intent(getActivity(), DetalleUsuario.class);
                                intent.putExtra("codigo", codigo);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("apellido", apellido);
                                intent.putExtra("correo", correo);
                                intent.putExtra("usuario", usuario);
                                intent.putExtra("clave", clave);
                                intent.putExtra("tipo", tipo);
                                intent.putExtra("estado", estadou);
                                intent.putExtra("pregunta", pregunta);
                                intent.putExtra("respuesta", respuesta);

                                startActivity(intent);
                            }
                        });

                        //Log.i("Id Categoria:    ", String.valueOf(objCategorias.getIdCategoria()));
                        //Log.i("Nombre:    ", String.valueOf(objCategorias.getNombre()));

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