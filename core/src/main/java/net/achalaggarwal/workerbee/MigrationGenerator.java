package net.achalaggarwal.workerbee;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

import static net.achalaggarwal.workerbee.QueryGenerator.create;
import static net.achalaggarwal.workerbee.QueryGenerator.drop;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class MigrationGenerator {

  private static Logger LOGGER = Logger.getLogger(MigrationGenerator.class.getName());

  public static void generateFilesFor(Database database, File baseDir) throws IOException {
    baseDir.mkdirs();

    Table[] tables = database.getTables();

    HashSet<MigrationVersion> existingMigrations = getExistingMigrationVersions(baseDir);

    for (Table table : tables) {
      MigrationVersion migration = new MigrationVersion(new Date().getTime(), table);

      if (!existingMigrations.contains(migration)){
        String filename = migration.getFileName();
        File file = new File(baseDir, filename);
        LOGGER.info("Writing migration for table : " + table.getName() + " at " + file.getAbsolutePath());
        FileUtils.writeStringToFile(file, tableMigrationStatements(table));
      } else {
        LOGGER.info("Table : " + table.getName() + " doesn't require to be migrated.");
      }
    }
  }

  private static String tableMigrationStatements(final Table table){
    ArrayList list = new ArrayList<String>() {{
      add(QueryGenerator.drop(table).ifExist().generate());
      add(QueryGenerator.create(table).generate());
      add(QueryGenerator.recover(table).generate());
    }};
    return Utils.joinList(list, " ");
  }

  private static HashSet<MigrationVersion> getExistingMigrationVersions(File baseDir) {
    File[] existingMigrations = baseDir.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().endsWith(".hql");
      }
    });

    HashSet<MigrationVersion> migrationVersions = new HashSet<>();

    for (File existingMigration : existingMigrations) {
      migrationVersions.add(MigrationVersion.fromFile(existingMigration));
    }

    return migrationVersions;
  }

  private static class MigrationVersion {
    private String timestamp;
    private Long version;
    private String tableName;

    public MigrationVersion(long timestamp, Table table) {
      this(valueOf(timestamp), table.getVersion(), table.getName());
    }

    public static MigrationVersion fromFile(File file) {
      String fileName = Utils.rtrim(file.getName(), ".hql");

      String[] parts = fileName.split("_", 3);
      return new MigrationVersion(parts[0], Long.parseLong(parts[1]), parts[2]);
    }

    private MigrationVersion(String timestamp, long version, String tableName) {
      this.timestamp = timestamp;
      this.version = version;
      this.tableName = tableName;
    }

    public String getFileName() {
      return format("%s_%s_%s.hql", timestamp, version, tableName);
    }

    @Override
    public int hashCode() {
      return tableName.hashCode() * version.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof MigrationVersion)) {
        return false;
      }

      MigrationVersion mv = (MigrationVersion) obj;

      return mv.tableName.equals(tableName) && mv.version <= version;
    }
  }
}