package dev.madela.apteka.resolvers;

import com.netflix.graphql.dgs.*;
import dev.madela.apteka.model.*;
import dev.madela.apteka.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * GraphQL-резолвер для работы с пользователями.
 *
 * Поддерживает:
 * - Query: users (с фильтрацией и пагинацией)
 * - Mutation: deleteUser
 *
 * Автор: MaDeLa
 */
@DgsComponent
@RequiredArgsConstructor
public class UserResolver {

    private final UserService userService;

    /**
     * Обработка GraphQL-запроса:
     * users(filter: UserFilter, pagination: Pagination): UserPage!
     *
     * Позволяет фильтровать пользователей по роли и дате регистрации.
     */
    @DgsQuery
    public UserPage users(
            @InputArgument UserFilter filter,
            @InputArgument Pagination pagination
    ) {
        return userService.getUsers(filter, pagination);
    }

    /**
     * Обработка GraphQL-мутации:
     * deleteUser(id: ID!): DeletedUserResult!
     *
     * Удаляет пользователя по ID и возвращает результат с отменёнными заказами.
     */
    @DgsMutation
    public DeletedUserResult deleteUser(@InputArgument String id) {
        return userService.deleteUser(id);
    }
}


/***
 * Краткое объяснение используемых DGS-аннотаций:
 * Аннотация	|	Назначение
 * @DgsComponent	|	Делает класс GraphQL-компонентом. Аналог @Component, но для DGS.
 * @DgsQuery	|	Помечает метод как обработчик GraphQL Query (из type Query {})	|
 * @DgsMutation	|	Помечает метод как обработчик GraphQL Mutation (type Mutation {})
 * @InputArgument	|	Извлекает аргумент из запроса GraphQL, аналог @RequestParam в REST
 * @DgsData	|	Обрабатывает отдельное поле объекта, например, виртуальное или внешнее
 * DgsDataFetchingEnvironment	|	Контекст запроса, через который можно получить source, DataLoader, headers и пр.
 */