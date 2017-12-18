package halfardawid.notepadx.activity.sketch_editor.colorpalette;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.R;

public class ColorPaletteActivityTab extends AppCompatActivity implements ColorSliderResponseInterface {

    static private final String TAG="COLOR_PALETTE";
    static public final int CODE=515;
    static public final String EXTRA_COLOR="COLOR";
    public static final int DEFAULT_COLOR = Color.BLACK;

    private ColorPreview colorPreview;
    private final Intent result=new Intent();
    private AtomicInteger color=new AtomicInteger(DEFAULT_COLOR);

    List<View> refresher=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette_tab);
        setResult(RESULT_OK,result);
        colorPreview= (ColorPreview) findViewById(R.id.acpt_color_preview);
        loadColorIntent();
        Toolbar toolbar = setupToolbar();
        setupSpinner(toolbar);
        setupButtons();
    }

    private void setupButtons() {
        findViewById(R.id.acpt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.acpt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,new Intent());
                finish();
            }
        });
    }

    private void setupSpinner(Toolbar toolbar) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new CustomAdapter(toolbar.getContext(),
                getResources().getStringArray(R.array.cpat_color_picking_modes)));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private Toolbar setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return toolbar;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_color_palette_activity_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private static class CustomAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public CustomAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private static int[] possible_fragments=new int[]{
                R.layout.fragment_cpat_hsv,
                R.layout.fragment_cpat_hsv_sliders,
                R.layout.fragment_cpat_rgb_sliders
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final Context c=getContext();
            final boolean b = c instanceof ColorSliderResponseInterface;
            ColorSliderResponseInterface c1 = (b)?(ColorSliderResponseInterface) c : null;
            if(b) c1.clearRefreshers();
            View rootView = inflater.inflate(possible_fragments[getArguments().getInt(ARG_SECTION_NUMBER)], container, false);
            //TODO: Think about securing it... or maybe just let it crash, a crash might be picked up faster...
            if(b) c1.refreshAll();
            return rootView;
        }

    }

    @Override
    public int getColor(){
        return color.get();
    }

    @Override
    public void setColor(int val){
        color.set(val);
        refreshAll();
        if(colorPreview!=null)
            colorPreview.setColor(val);
        result.putExtra(EXTRA_COLOR,color.get());
    }

    public void refreshAll() {
        synchronized (refresher) {
            for (View v : refresher) {
                v.invalidate();
            }
        }
    }

    private void loadColorIntent() {
        setColor(getIntent().getIntExtra(EXTRA_COLOR,DEFAULT_COLOR));
    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        setColor(s.getInt(EXTRA_COLOR,DEFAULT_COLOR));
    }

    @Override
    protected void onSaveInstanceState(Bundle s) {
        s.putInt(EXTRA_COLOR,color.get());
    }

    @Override
    public void addToRefresher(View view) {
        synchronized (refresher) {
            refresher.add(view);
        }
    }

    @Override
    public void clearRefreshers() {
        synchronized (refresher){
            refresher.clear();
        }
    }
}
