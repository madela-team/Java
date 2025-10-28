from sqlalchemy import Column, String, Date
from sqlalchemy.ext.declarative import declarative_base

# Создаём базовый класс для всех моделей SQLAlchemy
Base = declarative_base()

# Модель пользователя, соответствующая таблице "users" в БД
class User(Base):
    __tablename__ = "users"  # Название таблицы в базе данных

    id = Column(String, primary_key=True)         # Уникальный идентификатор пользователя (UUID в виде строки)
    name = Column(String)                         # Имя пользователя
    email = Column(String)                        # Email (уникальный, см. миграции)
    role = Column(String)                         # Роль (например, CUSTOMER, ADMIN и т.п.)
    registration_date = Column(Date)              # Дата регистрации (без времени)
