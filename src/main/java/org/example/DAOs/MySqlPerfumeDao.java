package org.example.DAOs;

import com.google.gson.Gson;
import org.example.DTOs.Perfume;
import org.example.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlPerfumeDao extends MySqlDao implements PerfumeDaoInterface {
    @Override
    public List<Perfume> findAllPerfume() throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Perfume> perfumeList = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM perfume";
            ps = connection.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                String gender = resultSet.getString("gender");
                int stockLvl = resultSet.getInt("stockLvl");
                Perfume p = new Perfume(_id, brand, name, size, price, gender, stockLvl);
                perfumeList.add(p);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllPerfumeResultSet() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return perfumeList;     // may be empty

    }


    @Override
    public String findAllPerfumeJSON() throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Perfume> perfumeList = new ArrayList<>();
        Gson gsonParser = new Gson();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM perfume";
            ps = connection.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                String gender = resultSet.getString("gender");
                int stockLvl = resultSet.getInt("stockLvl");
                Perfume p = new Perfume(_id, brand, name, size, price, gender, stockLvl);
                perfumeList.add(p);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllPerfumeResultSet() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }

        String Result = gsonParser.toJson(perfumeList);    // Serialize an object

        return Result;     // may be empty

    }


    @Override
    public List<Perfume> findAllPerfumeSubXEuro(float x) throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Perfume> perfumeList = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM perfume where price < ?";
            ps = connection.prepareStatement(query);
            ps.setFloat(1, x);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                String gender = resultSet.getString("gender");
                int stockLvl = resultSet.getInt("stockLvl");
                Perfume p = new Perfume(_id, brand, name, size, price, gender, stockLvl);
                perfumeList.add(p);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllPerfumeSubXEuro() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllPerfumeSubXEuro() " + e.getMessage());
            }
        }
        return perfumeList;     // may be empty

    }


    @Override
    public Perfume findPerfumeByID(String id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Perfume perfume = null;
        try
        {
            connection = this.getConnection();

            String query = "SELECT * FROM perfume WHERE _id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                String gender = resultSet.getString("gender");
                int stockLvl = resultSet.getInt("stockLvl");

                perfume = new Perfume(_id, brand, name, size, price, gender, stockLvl);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findPerfumeByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findPerfumeByID() " + e.getMessage());
            }
        }
        return perfume;     // reference to User object, or null value
    }


    @Override
    public String findPerfumeByIDJSON(String id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Perfume perfume = null;
        Gson gsonParser = new Gson();
        try
        {
            connection = this.getConnection();

            String query = "SELECT * FROM perfume WHERE _id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                String gender = resultSet.getString("gender");
                int stockLvl = resultSet.getInt("stockLvl");

                perfume = new Perfume(_id, brand, name, size, price, gender, stockLvl);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findPerfumeByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findPerfumeByID() " + e.getMessage());
            }
        }
        String Result = gsonParser.toJson(perfume);    // Serialize an object

        return Result;     // may be empty
    }


    @Override
    public void deletePerfumeByID(String id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Perfume perfume = null;
        try
        {
            connection = this.getConnection();

            String query = "DELETE FROM perfume WHERE _id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e)
        {
            throw new DaoException("deletePerfumeByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("deletePerfumeByID() " + e.getMessage());
            }
        }

    }

    @Override
    public void addPerfume(String brand, String name, int size, float price, String gender, int stockLvl) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            connection = this.getConnection();

            String query = "INSERT INTO perfume VALUES (null, ?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, brand);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, size);
            preparedStatement.setFloat(4, price);
            preparedStatement.setString(5, gender);
            preparedStatement.setInt(6, stockLvl);

            preparedStatement.executeUpdate();

        } catch (SQLException e)
        {
            throw new DaoException("addPerfume() " + e.getMessage());
        } finally
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("addPerfume() " + e.getMessage());
            }
        }

    }

}