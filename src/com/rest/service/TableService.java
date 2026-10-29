package com.rest.service;
import com.rest.models.Table;
import com.rest.repository.TableRepository;
import java.util.List;
public class TableService{
    private final TableRepository repository;
    public TableService(){
        this.repository = new TableRepository();
    }

    public TableService(TableRepository repository){
        this.repository = repository;
    }

    public TableRepository getRepository(){
        return repository;
    }

    public List<Table> getAllTables(){
        return repository.findAll();
    }

    public Table getTableById(int id){
        return repository.findById(id);
    }

    public boolean occupyTable(int id){
        Table table = repository.findById(id);
        if (table != null) {
            table.occupyTable();
            return true;
        }
        return false;
    }

    public boolean freeTable(int id){
        Table table = repository.findById(id);
        if (table != null) {
            table.freeTable();
            return true;
        }
        return false;
    }
}