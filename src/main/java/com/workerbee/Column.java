package com.workerbee;

public class Column implements Comparable {
  public static enum Type {
    INT {
      @Override
      public Object parseValue(RecordParser recordParser, int index) {
        return recordParser.readInt(index);
      }
    },
    STRING {
      @Override
      public Object parseValue(RecordParser recordParser, int index) {
        return recordParser.readString(index);
      }
    };

    public abstract Object parseValue(RecordParser rowParser, int index);

  }
  private final String name;

  private final Type type;
  private final String comment;
  private final Table belongsTo;

  public Column(Table belongsTo, String name, Type type) {
    this(belongsTo, name, type, null);
  }

  public Column(Table belongsTo, String name, Type type, String comment) {
    this.name = name;
    this.type = type;
    this.comment = comment;
    this.belongsTo = belongsTo;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getComment() {
    return comment;
  }

  public Table getBelongsTo() {
    return belongsTo;
  }

  public String getFqColumnName(){
    return Utils.fqColumnName(belongsTo, this);
  }

  public Object parseValueUsing(RecordParser recordParser, int index) {
    return type.parseValue(recordParser, index);
  }

  @Override
  public BooleanExpression eq(Comparable rightComparable) {
    return new BooleanExpression(this, "=", rightComparable);
  }

  @Override
  public BooleanExpression notEq(Comparable rightComparable) {
    return new BooleanExpression(this, "<>", rightComparable);
  }

  @Override
  public BooleanExpression gt(Comparable rightComparable) {
    return new BooleanExpression(this, ">", rightComparable);
  }

  @Override
  public BooleanExpression lt(Comparable rightComparable) {
    return new BooleanExpression(this, "<", rightComparable);
  }

  @Override
  public BooleanExpression gte(Comparable rightComparable) {
    return new BooleanExpression(this, ">=", rightComparable);
  }

  @Override
  public BooleanExpression lte(Comparable rightComparable) {
    return new BooleanExpression(this, "<=", rightComparable);
  }

  @Override
  public String operandName() {
    return getFqColumnName();
  }
}
