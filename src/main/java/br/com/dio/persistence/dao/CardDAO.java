package br.com.dio.persistence.dao; // linha 1

import br.com.dio.dto.CardDetailsDTO; // linha 2
import br.com.dio.persistence.entity.BoardColumnEntity; // linha 3
import br.com.dio.persistence.entity.CardEntity; // linha 4
import com.mysql.cj.jdbc.StatementImpl; // linha 5
import lombok.AllArgsConstructor; // linha 6

import java.sql.Connection; // linha 7
import java.sql.SQLException; // linha 8
import java.util.Optional; // linha 9

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime; // linha 10
import static java.util.Objects.nonNull; // linha 11

@AllArgsConstructor // linha 13
public class CardDAO { // linha 14

    private Connection connection; // linha 16

    public CardEntity insert(final CardEntity entity) throws SQLException { // linha 18
        var sql = "INSERT INTO CARDS (title, description, column_order, board_column_id) values (?, ?, ?, ?);"; // linha 19
        try(var statement = connection.prepareStatement(sql)){ // linha 20
            var i = 1; // linha 21
            statement.setString(i++, entity.getTitle()); // linha 22
            statement.setString(i++, entity.getDescription()); // linha 23
            statement.setInt(i++, entity.getColumnOrder()); // linha 24
            statement.setLong(i, entity.getBoardColumn().getId()); // linha 25
            statement.executeUpdate(); // linha 26
            if (statement instanceof StatementImpl impl){ // linha 27
                entity.setId(impl.getLastInsertID()); // linha 28
            } // linha 29
        } // linha 30
        return entity; // linha 31
    } // linha 32

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException{ // linha 34
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;"; // linha 35
        try(var statement = connection.prepareStatement(sql)){ // linha 36
            var i = 1; // linha 37
            statement.setLong(i ++, columnId); // linha 38
            statement.setLong(i, cardId); // linha 39
            statement.executeUpdate(); // linha 40
        } // linha 41
    } // linha 42

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException { // linha 44
        var sql = // linha 45
                """ 
                SELECT c.id, 
                       c.title, 
                       c.description, 
                       b.blocked_at, 
                       b.block_reason, 
                       c.board_column_id, 
                       bc.name, 
                       (SELECT COUNT(sub_b.id) 
                               FROM BLOCKS sub_b 
                              WHERE sub_b.card_id = c.id) blocks_amount 
                  FROM CARDS c 
                  LEFT JOIN BLOCKS b 
                    ON c.id = b.card_id 
                   AND b.unblocked_at IS NULL 
                 INNER JOIN BOARDS_COLUMNS bc 
                    ON bc.id = c.board_column_id 
                  WHERE c.id = ?; 
                """;
        try(var statement = connection.prepareStatement(sql)){ // linha 65
            statement.setLong(1, id); // linha 66
            statement.executeQuery(); // linha 67
            var resultSet = statement.getResultSet(); // linha 68
            if (resultSet.next()){ // linha 69
                var dto = new CardDetailsDTO( // linha 70
                        resultSet.getLong("c.id"), // linha 71
                        resultSet.getString("c.title"), // linha 72
                        resultSet.getString("c.description"), // linha 73
                        nonNull(resultSet.getString("b.block_reason")), // linha 74
                        toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")), // linha 75
                        resultSet.getString("b.block_reason"), // linha 76
                        resultSet.getInt("blocks_amount"), // linha 77
                        resultSet.getLong("c.board_column_id"), // linha 78
                        resultSet.getString("bc.name") // linha 79
                ); // linha 80
                return Optional.of(dto); // linha 81
            } // linha 82
        } // linha 83
        return Optional.empty(); // linha 84
    } // linha 85

    public CardEntity findLastInserted() throws SQLException { // linha 87
        var query = "SELECT * FROM cards ORDER BY id DESC LIMIT 1"; // linha 88
        try (var statement = connection.prepareStatement(query); // linha 89
             var resultSet = statement.executeQuery()) { // linha 90
            if (resultSet.next()) { // linha 91
                var entity = new CardEntity(); // linha 92
                entity.setId(resultSet.getLong("id")); // linha 93
                entity.setTitle(resultSet.getString("title")); // linha 94
                entity.setDescription(resultSet.getString("description")); // linha 95
                var column = new BoardColumnEntity(); // linha 96
                column.setId(resultSet.getLong("board_column_id")); // linha 97
                entity.setBoardColumn(column); // linha 98
                return entity; // linha 99
            } // linha 100
            return null; // linha 101
        } // linha 102
    } // linha 103

} // linha 105
