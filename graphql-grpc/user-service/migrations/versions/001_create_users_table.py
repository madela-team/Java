from alembic import op
import sqlalchemy as sa

# Уникальный идентификатор текущей миграции
revision = '001_create_users_table'

# Указывает на предыдущую миграцию. Так как это первая миграция — значение None.
down_revision = None

# Метки веток и зависимости в Alembic (не используются в данной миграции)
branch_labels = None
depends_on = None

def upgrade():
    """
    Метод upgrade() выполняется при применении миграции.
    Здесь создаётся таблица 'users' с базовыми пользовательскими данными.
    """
    op.create_table(
        'users',
        sa.Column('id', sa.String(length=36), primary_key=True),
        # Идентификатор пользователя в формате UUID (в виде строки длиной 36 символов)

        sa.Column('name', sa.String(length=255), nullable=False),
        # Имя пользователя

        sa.Column('email', sa.String(length=255), nullable=False, unique=True),
        # Email. Обязательное поле. Уникальное — не допускает дубликатов

        sa.Column('role', sa.String(length=50), nullable=False),
        # Роль пользователя в системе (например, "admin", "user", "moderator")

        sa.Column('registration_date', sa.Date(), nullable=False)
        # Дата регистрации. Без времени
    )

def downgrade():
    """
    Метод downgrade() вызывается при откате миграции.
    Здесь удаляется таблица 'users'.
    """
    op.drop_table('users')
