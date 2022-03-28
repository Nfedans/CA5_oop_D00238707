package org.example.DAOs;

import org.example.DTOs.Perfume;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface PerfumeDaoInterface {
    public List<Perfume> findAllPerfume() throws DaoException;

    public Perfume findPerfumeByID(String id) throws DaoException;

    public void deletePerfumeByID(String id) throws DaoException;

//    public User findUserByUsernamePassword(String username, String password) throws DaoException;
//
//    public List<User> findAllUsersLastNameContains(String subString ) throws DaoException;
//
//    public void addUser(User user) throws DaoException;
//
//    public void updatePassword(String username, String password) throws DaoException;

    /*
    public void deleteByLastName(String username, String lastName) throws DaoException;
    */
}
