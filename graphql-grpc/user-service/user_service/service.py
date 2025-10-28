import uuid
from datetime import datetime

from user_service import user_pb2, user_pb2_grpc
from user_service.models import User
from user_service.db import SessionLocal

class UserService(user_pb2_grpc.UserServiceServicer):
    def GetUserById(self, request, context):
        # Создаём сессию с базой данных
        db = SessionLocal()
        # Ищем пользователя по ID
        user = db.query(User).filter(User.id == request.id).first()
        db.close()
        if user:
            # Если найден — возвращаем ответ с данными пользователя
            return user_pb2.UserResponse(id=user.id, name=user.name, email=user.email)
        # Если не найден — возвращаем ошибку gRPC с кодом 404
        context.abort(404, "User not found")

    def CreateUser(self, request, context):
        db = SessionLocal()
        # Создаём нового пользователя с новым UUID
        new_user = User(id=str(uuid.uuid4()), name=request.name, email=request.email)
        db.add(new_user)
        db.commit()
        db.refresh(new_user)  # Обновляем объект из БД, чтобы получить актуальные значения
        db.close()
        # Возвращаем данные созданного пользователя
        return user_pb2.UserResponse(id=new_user.id, name=new_user.name, email=new_user.email)

    def GetUserList(self, request, context):
        db = SessionLocal()

        query = db.query(User)

        # Если задан фильтр по роли — применяем его
        if request.role:
            query = query.filter(User.role == request.role)

        # Если задан фильтр по дате регистрации "после" — применяем его
        if request.registration_after:
            after_date = datetime.strptime(request.registration_after, "%Y-%m-%d").date()
            query = query.filter(User.registration_date >= after_date)

        total_count = query.count()
        # Пагинация: пропускаем записи и выбираем ограниченное количество
        users = query.offset(request.page * request.size).limit(request.size).all()

        response = user_pb2.UserListResponse()
        response.total_count = total_count

        for user in users:
            # Добавляем каждого пользователя в ответ
            response.users.add(
                id=user.id,
                name=user.name,
                email=user.email,
                role=user.role,
                registration_date=user.registration_date.strftime("%Y-%m-%d")
            )

        db.close()
        return response

    def DeleteUser(self, request, context):
        db = SessionLocal()
        user = db.query(User).filter(User.id == request.id).first()

        if not user:
            db.close()
            # Если пользователя нет — возвращаем ошибку 404
            context.abort(404, "User not found")

        # Удаляем пользователя из базы
        db.delete(user)
        db.commit()
        db.close()

        # Возвращаем подтверждение удаления с ID и именем пользователя
        return user_pb2.DeleteUserResponse(
            id=user.id,
            name=user.name
        )
