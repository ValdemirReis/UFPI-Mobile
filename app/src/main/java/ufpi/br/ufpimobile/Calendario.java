package ufpi.br.ufpimobile;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ufpi.br.ufpimobile.model.CalendarioDAO;

public class Calendario extends AppCompatActivity {

    private static final String TAG = "TelaHome";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy ", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    private Toolbar toolbar;
    private CompactCalendarView compactCalendarView;
    private boolean shouldShow = false;

    public RequestQueue queue;
    private List<CalendarioDAO> listCalendario;
    String url = "https://ufpi-mobile-cm.herokuapp.com/api/calendars/5a4ba0252345bc00043e9b3a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        final List<String> informacoes = new ArrayList<>();

        final ListView datasListView = (ListView) findViewById(R.id.datas_listview);
        final Button slideCalendarBut = (Button) findViewById(R.id.slide_calendar);

        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, informacoes);
        datasListView.setAdapter(adapter);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);


        //Alterar o texto da toolbar para a data selecionada
        toolbar = (Toolbar) findViewById(R.id.toolbar_Calendario);
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), TelaHome.class);
                finish();
                startActivity(home);
            }
        });

        queue = Volley.newRequestQueue(this);
        fetchPosts();


        // Definir o título na rolagem do calendário
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                toolbar.setTitle(dateFormatForMonth.format(dateClicked));
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                if (bookingsFromMap != null) {
                    Log.d(TAG, bookingsFromMap.toString());
                    informacoes.clear();
                    for (Event booking : bookingsFromMap) {
                        informacoes.add((String) booking.getData());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));


            }
        });
        final View.OnClickListener showCalendarOnClickLis = getCalendarShowLis();
        slideCalendarBut.setOnClickListener(showCalendarOnClickLis);

        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {
                slideCalendarBut.setText("Minimizar");

            }

            @Override
            public void onClosed() {
                slideCalendarBut.setText("Maximizar");
            }

        });

    }


    private View.OnClickListener getCalendarShowLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendar();
                    } else {
                        compactCalendarView.hideCalendar();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostLoaded, onPostsError);
        queue.add(request);
    }

    private final Response.Listener<String> onPostLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Type listType = new TypeToken<ArrayList<CalendarioDAO>>() {
            }.getType();
            listCalendario = new Gson().fromJson(response, listType);

            System.out.println("Entrou karai       ***            **8\n");

            adicionarDatas();
        }

    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

    public void adicionarDatas() {

        List<Event> eventos = new ArrayList<>();

        /*//13 DE ABRIL
        //É necessário converter a data em https://www.epochconverter.com/

        Event abril[] = new Event[10];

        abril[0]= new Event(Color.RED, 1492126868000L, "Desenvolvimento Mobile !!");
        abril[1] = new Event(Color.RED, 1492126868000L, "UFPI Mobile !!");
        abril[3]= new Event(Color.BLUE, 1491349268000L, "Colégio Técnico de Teresina - CTT");
        compactCalendarView.addEvent(abril[0]);
        compactCalendarView.addEvent(abril[1]);
        compactCalendarView.addEvent(abril[3]);

        //************
        //14 DE ABRIL
        Event ev4 = new Event(Color.BLUE,  1492213268000L, "CTT Mobile !!");
        compactCalendarView.addEvent(ev4);

        //7 de set
        Event setemb[]=new Event[10];
        setemb[0]= new Event(Color.RED, 1504656000000L, "Universidade Federal do Piauí - UFPI");
        compactCalendarView.addEvent(setemb[0]);
        setemb[1]= new Event(Color.BLUE, 1504656000000L, "UFPI Mobile");
        compactCalendarView.addEvent(setemb[1]);*/





        if (listCalendario == null) {
            System.out.println("Lista Nulla!!");
        } else {
            for (CalendarioDAO calen : listCalendario) {
                for (CalendarioDAO.EventsBean event : calen.getEvents()) {

                    if (event.getEndTime() != 0){
                        eventos.add(new Event(Color.RED, event.getStartTime(), event.getTitle()));
                        eventos.add(new Event(Color.RED, event.getEndTime(), event.getTitle()));
                    }
                    eventos.add(new Event(Color.BLUE, event.getStartTime(), event.getTitle()));
                    //System.out.println("\n" + event.getTitle() + " --> " + event.getStartTime());
                }
            }

            compactCalendarView.addEvents(eventos);

        }


    }
}
