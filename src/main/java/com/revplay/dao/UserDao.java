package com.revplay.dao;



import com.revplay.model.User;

import java.sql.*;



public class UserDao {



    public boolean registerUser(User user) throws SQLException {

        String sql = "{call register_user(?,?,?,?)}";

        try (Connection conn = ConnectionFactory.getConnection();

             CallableStatement cs = conn.prepareCall(sql)) {



            cs.setString(1, user.getUsername());

            cs.setString(2, user.getEmail());

            cs.setString(3, user.getPasswordHash());

            cs.setString(4, user.isArtist() ? "Y" : "N");



            cs.execute();

            return true;

        }

    }





    public User loginUser(String username, String password) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {



            ps.setString(1, username);

            ps.setString(2, password);



            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getLong("user_id"));

                    user.setUsername(rs.getString("username"));

                    user.setEmail(rs.getString("email"));

                    user.setArtist("Y".equals(rs.getString("is_artist")));

                    user.setSecurityQuestions(rs.getString("security_question"));

                    return user;

                }

            }

        }

        return null;

    }



    public User getUserById(long userId) throws SQLException {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {



            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getLong("user_id"));

                    user.setUsername(rs.getString("username"));

                    user.setEmail(rs.getString("email"));

                    user.setArtist("Y".equals(rs.getString("is_artist")));

                    user.setSecurityQuestions(rs.getString("security_question"));

                    return user;

                }

            }

        }

        return null;

    }



    public boolean updatePassword(long userId, String newPasswordHash) throws SQLException {

        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {



            ps.setString(1, newPasswordHash);

            ps.setLong(2, userId);

            return ps.executeUpdate() > 0;

        }

    }



    public boolean updateSecurityInfo(long userId, String securityQuestion, String securityAnswer) throws SQLException {

        String sql = "UPDATE users SET security_question = ?, security_answer = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {



            ps.setString(1, securityQuestion);

            ps.setString(2, securityAnswer);

            ps.setLong(3, userId);

            return ps.executeUpdate() > 0;

        }

    }



    public User getUserByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {



            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getLong("user_id"));

                    user.setUsername(rs.getString("username"));

                    user.setEmail(rs.getString("email"));

                    user.setArtist("Y".equals(rs.getString("is_artist")));

                    user.setSecurityQuestions(rs.getString("security_question"));

                    return user;

                }

            }

        }

        return null;

    }



}

