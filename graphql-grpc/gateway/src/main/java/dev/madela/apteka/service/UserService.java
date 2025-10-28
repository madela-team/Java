package dev.madela.apteka.service;

import dev.madela.apteka.model.*;
import dev.madela.apteka.proto.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями.
 * Выполняет взаимодействие с gRPC-сервисом пользователей (user-service).
 *
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceGrpc.UserServiceBlockingStub grpcClient;

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId ID пользователя
     * @return объект User или null, если пользователь не найден
     */
    public User getUserById(String userId) {
        var request = UserIdRequest.newBuilder().setId(userId).build();
        var response = grpcClient.getUserById(request);
        if (response == null) {
            return null;
        }
        return toDto(response);
    }

    /**
     * Получает список пользователей по фильтру и постранично.
     * Используется для вывода таблиц пользователей в UI и для аналитики.
     *
     * @param filter     фильтр по роли и дате регистрации
     * @param pagination параметры пагинации (номер страницы и размер)
     * @return страница пользователей с totalCount и списком элементов
     */
    public UserPage getUsers(UserFilter filter, Pagination pagination) {
        var grpcRequest = UserListRequest.newBuilder()
                .setRole(filter.getRole() != null ? filter.getRole() : "")
                .setRegistrationBefore(
                        filter.getRegistrationDate() != null && filter.getRegistrationDate().getBefore() != null
                                ? filter.getRegistrationDate().getBefore().toString()
                                : ""
                )
                .setRegistrationAfter(
                        filter.getRegistrationDate() != null && filter.getRegistrationDate().getAfter() != null
                                ? filter.getRegistrationDate().getAfter().toString()
                                : ""
                )
                .setPage(pagination.getPage())
                .setSize(pagination.getSize())
                .build();

        var grpcResponse = grpcClient.getUserList(grpcRequest);

        List<User> users = grpcResponse.getUsersList().stream()
                .map(this::mapToUser)
                .collect(Collectors.toList());

        return UserPage.builder()
                .items(users)
                .totalCount(grpcResponse.getTotalCount())
                .build();
    }

    /**
     * Удаляет пользователя по ID.
     * При этом возвращается список заказов, которые были отменены в связи с удалением.
     *
     * @param id ID пользователя
     * @return результат удаления с именем пользователя и списком отменённых заказов
     */
    public DeletedUserResult deleteUser(String id) {
        var grpcRequest = DeleteUserRequest.newBuilder().setId(id).build();
        var grpcResponse = grpcClient.deleteUser(grpcRequest);

        List<Order> cancelledOrders = grpcResponse.getCancelledOrdersList().stream()
                .map(o -> Order.builder()
                        .id(o.getId())
                        .status(o.getStatus())
                        .build())
                .collect(Collectors.toList());

        return DeletedUserResult.builder()
                .id(grpcResponse.getId())
                .name(grpcResponse.getName())
                .cancelledOrders(cancelledOrders)
                .build();
    }

    /**
     * Преобразует gRPC-ответ в простой объект User (без роли и даты).
     * Используется там, где эти поля не нужны.
     */
    private User toDto(UserResponse response) {
        return User.builder()
                .id(response.getId())
                .name(response.getName())
                .email(response.getEmail())
                .build();
    }

    /**
     * Полный маппинг gRPC-ответа в User со всеми полями.
     * Используется, когда нужно больше информации — например, при выводе таблиц.
     */
    private User mapToUser(UserResponse userProto) {
        return User.builder()
                .id(userProto.getId())
                .name(userProto.getName())
                .email(userProto.getEmail())
                .role(userProto.getRole())
                .registrationDate(userProto.getRegistrationDate())
                .build();
    }
}
