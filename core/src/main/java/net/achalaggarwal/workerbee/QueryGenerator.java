package net.achalaggarwal.workerbee;

import net.achalaggarwal.workerbee.ddl.create.DatabaseCreator;
import net.achalaggarwal.workerbee.ddl.create.TableCreator;
import net.achalaggarwal.workerbee.ddl.drop.DatabaseDropper;
import net.achalaggarwal.workerbee.ddl.drop.TableDropper;
import net.achalaggarwal.workerbee.ddl.misc.LoadData;
import net.achalaggarwal.workerbee.ddl.misc.RecoverPartition;
import net.achalaggarwal.workerbee.ddl.misc.TruncateTable;
import net.achalaggarwal.workerbee.dml.insert.InsertQuery;
import net.achalaggarwal.workerbee.dr.SelectFunction;
import net.achalaggarwal.workerbee.dr.SelectQuery;

public class QueryGenerator {
  public static DatabaseCreator create(Database database){
    return new DatabaseCreator(database);
  }

  public static DatabaseDropper drop(Database database) {
    return new DatabaseDropper(database);
  }

  public static TableCreator create(Table<? extends Table> table){
    return new TableCreator(table);
  }

  public static TableDropper drop(Table table) {
    return new TableDropper(table);
  }

  public static RecoverPartition recover(Table<? extends Table> table) {
    return new RecoverPartition(table);
  }

  public static SelectQuery select(SelectFunction... selectFunctions) {
    return new SelectQuery(selectFunctions);
  }

  public static InsertQuery insert() {
    return new InsertQuery();
  }

  public static InsertQuery insert(Row<? extends Table> row) {
    return new InsertQuery().using(select(row.getConstants()).from(Table.DUAL));
  }

  public static LoadData loadData() {
    return new LoadData();
  }

  public static TruncateTable truncate(Table<? extends Table> table) {
    return new TruncateTable(table);
  }
}
