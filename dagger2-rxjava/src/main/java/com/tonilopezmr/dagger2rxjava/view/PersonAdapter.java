package com.tonilopezmr.dagger2rxjava.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tonilopezmr.dagger2rxjava.R;
import com.tonilopezmr.dagger2rxjava.domain.Person;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio López.
 */
public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Person> persons;

    public PersonAdapter() {
        this.persons = new LinkedList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_row, parent, false);
        return new PersonViewHolder(view);
    }

    public void addAll(List<Person> persons){
        this.persons.addAll(persons);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PersonViewHolder personViewHolder = (PersonViewHolder) holder;
        Person person = persons.get(position);
        personViewHolder.render(person);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public PersonViewHolder(View view) {
            super(view);
            this.nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        }

        public void render(Person person){
            nameTextView.setText(person.getName());
        }
    }
}
