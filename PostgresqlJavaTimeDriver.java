package postgresql.driver;

import org.postgresql.Driver;
import org.postgresql.core.Oid;
import org.postgresql.core.TypeInfo;
import org.postgresql.jdbc.PgConnection;
import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Properties;

/***
 * Postgresql 사용 시 timestamp, date 등 타입에 대해 java.time.* 패키지를 사용하도록 지원
 * org.postgresql.Driver 대신 사용
 *
 * @author Jaehak Lee
 */
public class PostgresqlJavaTimeDriver extends Driver {

  static {
    try {
      Driver.deregister();
      DriverManager.registerDriver(new PostgresqlJavaTimeDriver());
      Enumeration<java.sql.Driver> en = DriverManager.getDrivers();
      while (en.hasMoreElements()) {
        Object driver = en.nextElement();
        OhcSysLog.fine(driver);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Override
  public @Nullable Connection connect(String url, @Nullable Properties info) throws SQLException {
    Connection conn = super.connect(url, info);
    assert conn instanceof PgConnection;
    TypeInfo typeInfo = ((PgConnection)conn).getTypeInfo();
//    typeInfo.addCoreType("timestamptz", Oid.TIMESTAMPTZ, Types.TIMESTAMP_WITH_TIMEZONE, "java.time.OffsetDateTime", Oid.TIMESTAMPTZ_ARRAY);
    typeInfo.addCoreType("timestamptz", Oid.TIMESTAMPTZ, Types.TIMESTAMP, "java.time.OffsetDateTime", Oid.TIMESTAMPTZ_ARRAY);
    typeInfo.addCoreType("timestamp", Oid.TIMESTAMP, Types.TIMESTAMP, "java.time.LocalDateTime", Oid.TIMESTAMP_ARRAY);
    typeInfo.addCoreType("date", Oid.DATE, Types.DATE, "java.time.LocalDate", Oid.DATE_ARRAY);
    return conn;
  }
}
