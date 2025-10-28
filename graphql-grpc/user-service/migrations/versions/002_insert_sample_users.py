from alembic import op
import sqlalchemy as sa
from datetime import date

# Уникальный идентификатор этой миграции
revision = '002_insert_sample_users'

# Указание на предыдущую миграцию (создание таблицы users)
down_revision = '001_create_users_table'

# Не используются в данной миграции
branch_labels = None
depends_on = None

def upgrade():
    """
    Метод upgrade() выполняется при применении миграции.
    Вставляет в таблицу 'users' тестовые (демонстрационные) записи.
    """
    op.execute("""
        INSERT INTO users (id, name, email, role, registration_date) VALUES
        ('user-001', 'Иван Петров', 'ivan.petrov@example.com', 'CUSTOMER', '2023-01-10'),
        ('user-002', 'Анна Смирнова', 'anna.smirnova@example.com', 'CUSTOMER', '2023-02-15'),
        ('user-003', 'Олег Сидоров', 'oleg.sidorov@example.com', 'CUSTOMER', '2023-03-20'),
        ('user-004', 'Екатерина Иванова', 'ekaterina.ivanova@example.com', 'CUSTOMER', '2023-04-05'),
        ('user-005', 'Дмитрий Кузнецов', 'dmitry.kuznetsov@example.com', 'CUSTOMER', '2023-05-12');
    """)

def downgrade():
    """
    Метод downgrade() вызывается при откате миграции.
    Удаляет тестовые записи пользователей, добавленные в upgrade().
    """
    op.execute("""
        DELETE FROM users WHERE id IN (
            'user-001', 'user-002', 'user-003', 'user-004', 'user-005'
        );
    """)
