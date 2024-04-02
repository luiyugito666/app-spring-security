package com.jorgeyh7.app;

import com.jorgeyh7.app.persistence.entity.PermissionEntity;
import com.jorgeyh7.app.persistence.entity.RoleEntity;
import com.jorgeyh7.app.persistence.entity.RoleEnum;
import com.jorgeyh7.app.persistence.entity.UserEntity;
import com.jorgeyh7.app.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}
	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return args->{
			//create permissions
			PermissionEntity createPermision= PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity readPermision= PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity updatePermision= PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity deletePermision= PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity refactorPermision= PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			//create roles

			RoleEntity roleAdmin= RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermision,readPermision,updatePermision,deletePermision))
					.build();
			RoleEntity roleUser= RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermision,readPermision))
					.build();
			RoleEntity roleInvited= RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(readPermision))
					.build();
			RoleEntity roleDeveloper= RoleEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(createPermision,readPermision,updatePermision,deletePermision,refactorPermision))
					.build();

			//create users

			UserEntity userJorge= UserEntity.builder()
					.username("jorge")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.accountNoLocked(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userWithney= UserEntity.builder()
					.username("withney")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.accountNoLocked(true)
					.roles(Set.of(roleUser))
					.build();

			UserEntity userYovani= UserEntity.builder()
					.username("yovani")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.accountNoLocked(true)
					.roles(Set.of(roleInvited))
					.build();

			UserEntity userKen= UserEntity.builder()
					.username("ken")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.credentialNoExpired(true)
					.accountNoLocked(true)
					.roles(Set.of(roleDeveloper))
					.build();

			userRepository.saveAll(List.of(userKen,userYovani,userWithney,userJorge ));


		};

	}

}
