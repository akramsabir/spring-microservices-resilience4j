package tp.com.service;

import tp.com.dto.UserRequestDTO;
import tp.com.dto.UserResponseDTO;
import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    void deleteUser(Long id);
}