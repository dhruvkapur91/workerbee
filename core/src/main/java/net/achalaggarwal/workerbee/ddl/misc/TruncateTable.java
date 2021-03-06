package net.achalaggarwal.workerbee.ddl.misc;

import net.achalaggarwal.workerbee.Query;
import net.achalaggarwal.workerbee.Table;

public class TruncateTable implements Query {
  private Table<? extends Table> table;

  public TruncateTable(Table<? extends Table> table) {
    this.table = table;
  }

  @Override
  public String generate() {
    StringBuilder result = new StringBuilder();

    result.append("USE " + table.getDatabaseName() + " ;");
    result.append("TRUNCATE TABLE " + table.getName() + " ;");

    return result.toString();
  }
}
