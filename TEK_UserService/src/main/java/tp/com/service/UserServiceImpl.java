package tp.com.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tp.com.dto.UserRequestDTO;
import tp.com.dto.UserResponseDTO;
import tp.com.entity.User;
import tp.com.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Helper: Map Entity to Response DTO
    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    // Helper: Map Request DTO to Entity
    private User mapToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        User user = mapToEntity(requestDTO);
        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}