package com.workerbee;

import org.junit.Test;

import java.sql.SQLException;

import static com.workerbee.Column.Type.FLOAT;
import static com.workerbee.Column.Type.INT;
import static com.workerbee.Column.Type.STRING;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ColumnTest {
  public static final String COLUMN_NAME = "COLUMN_NAME";
  public static final Integer INT_VALUE = 1;
  public static final int INDEX = 0;
  public static final String STRING_VALUE = "STRING_VALUE";
  public static final float FLOAT_VALUE = 2.3f;
  Column column = new Column(null, COLUMN_NAME, INT);

  @Test
  public void shouldCreateColumn(){
    assertThat(column, instanceOf(Column.class));
    assertThat(column.getName(), is(COLUMN_NAME));
    assertThat(column.getType(), is(INT));
  }

  @Test
  public void shouldParseIntegerValueUsingRecordParser() throws SQLException {
    RecordParser mockRecordParser = mock(RecordParser.class);

    when(mockRecordParser.getInt(INDEX)).thenReturn(INT_VALUE);

    assertThat((Integer) column.parseValueUsing(mockRecordParser, INDEX), is(INT_VALUE));

    verify(mockRecordParser).getInt(INDEX);
  }

  @Test
  public void shouldParseFloatValueUsingRecordParser() throws SQLException {
    Column column = new Column(null, COLUMN_NAME, FLOAT);
    RecordParser mockRecordParser = mock(RecordParser.class);

    when(mockRecordParser.getFloat(INDEX)).thenReturn(FLOAT_VALUE);

    assertThat((Float) column.parseValueUsing(mockRecordParser, INDEX), is(FLOAT_VALUE));

    verify(mockRecordParser).getFloat(INDEX);
  }

  @Test
  public void shouldParseStringValueUsingRecordParser() throws SQLException {
    Column column = new Column(null, COLUMN_NAME, STRING);
    RecordParser mockRecordParser = mock(RecordParser.class);

    when(mockRecordParser.getString(INDEX)).thenReturn(STRING_VALUE);

    assertThat((String) column.parseValueUsing(mockRecordParser, INDEX), is(STRING_VALUE));

    verify(mockRecordParser).getString(INDEX);
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowRuntimeExceptionIfValueCannotBeParse() throws SQLException {
    Column column = new Column(null, COLUMN_NAME, INT);
    RecordParser mockRecordParser = mock(RecordParser.class);

    when(mockRecordParser.getInt(INDEX)).thenThrow(new RuntimeException());
    column.parseValueUsing(mockRecordParser, INDEX);
  }

  @Test
  public void shouldBeAComparableGiveFqColumnWhenOperandNameIsAskedFor(){
    assertThat(column, instanceOf(com.workerbee.expression.Comparable.class));
    assertThat(column.operandName(), is(column.getFqColumnName()));
  }
}