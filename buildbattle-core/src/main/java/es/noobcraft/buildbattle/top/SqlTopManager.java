package es.noobcraft.buildbattle.top;

import es.noobcraft.buildbattle.api.top.TopManager;
import es.noobcraft.core.api.Core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqlTopManager implements TopManager {

    @Override
    public Map<String, Integer> getTop(int amount, String type) {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            String TOP_WINS = "SELECT * FROM build_battle_statistics ORDER BY "+ type+ " DESC LIMIT ?";
            try(PreparedStatement statement = connection.prepareStatement(TOP_WINS)) {
                statement.setInt(1, amount);

                try(ResultSet resultSet = statement.executeQuery()) {
                    Map<String, Integer> map = new LinkedHashMap<>();
                    while (resultSet.next()) {
                        map.put(resultSet.getString("username"),
                                resultSet.getInt(type));
                    }
                    return map;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
