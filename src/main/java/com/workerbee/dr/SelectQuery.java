package com.workerbee.dr;

import com.workerbee.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.workerbee.Utils.fqColumnName;

public class SelectQuery implements Query {
  private Table table;
  private List<SelectFunction> selectFunctions = new ArrayList<SelectFunction>();
  private String alias;
  private Table joinTable;
  private Expression onExpression;

  public SelectQuery(SelectFunction... selectFunctions){
    this.selectFunctions.addAll(Arrays.asList(selectFunctions));
  }

  public SelectQuery(List<SelectFunction> selectFunctions) {
    this.selectFunctions.addAll(selectFunctions);
  }

  public SelectQuery from(Table table){
    this.table = table;

    return this;
  }

  public SelectQuery join(Table table) {
    joinTable = table;
    return this;
  }

  public SelectQuery on(Expression expression){
    onExpression = expression;
    return this;
  }

  public SelectQuery as(String alias){
    this.alias = alias;

    return this;
  }

  public Table table(){
    Table table = new Table(alias);

    for (SelectFunction selectFunction : selectFunctions) {
      table.havingColumn(new Column(table, selectFunction.getAlias(), selectFunction.getType()));
    }

    return table;
  }

  @Override
  public String generate() {
    StringBuilder result = new StringBuilder();

    result.append("SELECT");

    if (!selectFunctions.isEmpty()){
      for (SelectFunction selectFunction : selectFunctions) {
        result.append(" " + selectFunction.generate() + ",");
      }
      result.delete(result.lastIndexOf(","), result.length());
    }

    result.append(" FROM ");

    result.append(Utils.fqTableName(table));

    if (joinTable != null && onExpression != null){
      result.append(" JOIN ");

      result.append(Utils.fqTableName(joinTable));

      result.append(" ON ");
      result.append(onExpression.generate());
    }

    if (alias != null){
      result.append(" AS " + alias);
    }

    result.append(" ;");

    return result.toString();
  }
}
