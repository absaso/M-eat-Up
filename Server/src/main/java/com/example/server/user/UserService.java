package com.example.server.user;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import com.example.server.Restaurents.RestaurantDAO;
import com.example.server.Traitement.TraitementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
	public final UserRepository userRepository;

	@Autowired
	private TraitementService traitementService;

	UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void AddUser(User user) {
		userRepository.save(user);
		traitementService.postUser(user);
	}

	public Optional<User> getUserById(int Id) {
		return userRepository.findById(Id);
	}

	public Optional<User> getUserByUsernameAndPassword(String username, String password) {
		return userRepository.findByLoginAndPwd(username, password);
	}

	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByLogin(username);
	}

	public void AddHistory(User user, String history) {
		if (user.getHistorique_name() == null) {
			user.setHistorique_name(new ArrayList<>());
		}

		List<String> historique = user.getHistorique_name();
		historique.add(history);
		user.setHistorique_name(historique);

		userRepository.save(user);
	}

	public void AddHistoryId(User user, String history) {
		if (user.getHistorique_id() == null) {
			user.setHistorique_id(new ArrayList<>());
		}

		List<String> historique = user.getHistorique_id();
		historique.add(history);
		user.setHistorique_id(historique);

		userRepository.save(user);
	}

	public void modifyPreference(User user, List<String> preference) {
		user.setPreferences(preference);
		userRepository.save(user);
	}

	public void addPreferences(String user_login, String restauran_id) {
		Optional<User> uOpt = userRepository.findByLogin(user_login);
		if (uOpt.isPresent()) {
			User user = uOpt.get();
			if (user.getRestaurants_suggere() == null) {
				user.setRestaurants_suggere(new ArrayList<>());

			}
			List<String> suggestions = user.getRestaurants_suggere();
			suggestions.add(restauran_id);
			user.setRestaurants_suggere(suggestions);
			userRepository.save(user);
		}

	}

	public User getUserDAO(UserDTO userDTO) {
		Optional<User> uOpt = userRepository.findByLogin(userDTO.getLogin());
		User user = uOpt.get();

		return user;

	}


	public void modifyLikedHistory(User user,List<String> history){
		if (user.getLiked_restaurants() == null) {
			user.setLiked_restaurants(new ArrayList<>());
		}

		List<String> liked_history = user.getLiked_restaurants();
		for (String h:history){
			liked_history.add(h);}
		user.setLiked_restaurants(liked_history);
		userRepository.save(user);
	}

}












